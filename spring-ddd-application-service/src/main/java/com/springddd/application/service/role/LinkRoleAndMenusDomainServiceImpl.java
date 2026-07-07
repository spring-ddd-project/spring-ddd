package com.springddd.application.service.role;

import com.springddd.application.service.auth.cache.UserDetailCacheService;
import com.springddd.application.service.role.dto.SysRoleMenuView;
import com.springddd.application.service.user.SysUserRoleQueryService;
import com.springddd.domain.menu.MenuId;
import com.springddd.domain.role.*;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LinkRoleAndMenusDomainServiceImpl implements LinkRoleAndMenusDomainService {

    private final SysRoleMenuQueryService sysRoleMenuQueryService;

    private final WipeSysRoleMenuByIdsDomainService wipeSysRoleMenuByIdsDomainService;

    private final SysRoleMenuDomainRepository sysRoleMenuDomainRepository;

    private final SysRoleMenuDomainFactory sysRoleMenuDomainFactory;

    private final SysUserRoleQueryService sysUserRoleQueryService;

    private final UserDetailCacheService userDetailCacheService;

    private final ReactiveRedisCacheHelper reactiveRedisCacheHelper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> link(Long roleId, List<Long> menuIds) {
        return sysRoleMenuQueryService.queryLinkRoleAndMenus(roleId).flatMap(sysRoleMenuViews -> {
            List<Long> ids = sysRoleMenuViews.stream().map(SysRoleMenuView::getId).toList();
            Mono<Void> deleted = wipeSysRoleMenuByIdsDomainService.deleteByIds(ids);

            List<Mono<Long>> saved = menuIds.stream().map(menuId -> {
                SysRoleMenuDomain domain = sysRoleMenuDomainFactory.newInstance(new RoleId(roleId), new MenuId(menuId), null);
                domain.create();
                return sysRoleMenuDomainRepository.save(domain);
            }).toList();

            return deleted
                    .thenMany(Mono.when(saved))
                    .then(clearUserCaches(roleId));
        });
    }

    /**
     * Clears authentication and menu caches for all users bound to the given role.
     * This guarantees that permission changes take effect on the user's next request
     * instead of waiting for the 7-day TTL to expire.
     */
    private Mono<Void> clearUserCaches(Long roleId) {
        return sysUserRoleQueryService.queryLinkUserAndRoleByRoleId(roleId)
                .flatMapMany(Flux::fromIterable)
                .map(userRole -> userRole.getUserId())
                .distinct()
                .flatMap(this::clearUserCache)
                .then();
    }

    private Mono<Void> clearUserCache(Long userId) {
        String withPermissionsKey = CacheKeys.MENU_WITH_PERMISSIONS.buildKey(userId);
        String withoutPermissionsKey = CacheKeys.MENU_WITHOUT_PERMISSIONS.buildKey(userId);
        return Mono.when(
                userDetailCacheService.delete(userId),
                reactiveRedisCacheHelper.deleteCache(withPermissionsKey),
                reactiveRedisCacheHelper.deleteCache(withoutPermissionsKey)
        ).onErrorResume(e -> Mono.empty()).then();
    }
}
