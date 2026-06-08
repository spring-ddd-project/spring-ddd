package com.springddd.application.service.menu;

import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.application.service.role.SysRoleMenuQueryService;
import com.springddd.application.service.role.dto.SysRoleMenuView;
import com.springddd.domain.menu.WipeSysMenuByIdsDomainService;
import com.springddd.domain.role.WipeSysRoleMenuByIdsDomainService;
import com.springddd.domain.util.ReactiveTreeUtils;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WipeSysMenuByIdsDomainServiceImpl implements WipeSysMenuByIdsDomainService {

    private final SysMenuRepository sysMenuRepository;

    private final SysMenuQueryService sysMenuQueryService;

    private final WipeSysRoleMenuByIdsDomainService wipeSysRoleMenuByIdsDomainService;

    private final SysRoleMenuQueryService sysRoleMenuQueryService;

    /**
     * Delete the associations between roles and menus when deleting the menu.
     *
     * @param ids menu ids
     * @return Void
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> deleteByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Mono.empty();
        }
        return sysMenuQueryService.queryAllMenu()
                .flatMapMany(menus ->
                        Flux.fromIterable(ids)
                                .filter(Objects::nonNull)
                                .flatMap(id -> {
                                    List<SysMenuView> allChildren = ReactiveTreeUtils.findAllChildrenFrom(
                                            id, menus, SysMenuView::getId, SysMenuView::getParentId);
                                    return CollectionUtils.isEmpty(allChildren) ? Mono.empty() : Flux.fromIterable(allChildren)
                                            .map(SysMenuView::getId)
                                            .filter(Objects::nonNull);
                                }).distinct())
                .collectList()
                .filter(menuIds -> !CollectionUtils.isEmpty(menuIds))
                .switchIfEmpty(Mono.empty())
                .flatMap(menuIds -> Flux.fromIterable(menuIds)
                        .flatMap(sysRoleMenuQueryService::queryLinkRoleAndMenusByMenuId)
                        .filter(Objects::nonNull)
                        .flatMap(Flux::fromIterable)
                        .map(SysRoleMenuView::getId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collectList()
                        .filter(rmIds -> !CollectionUtils.isEmpty(rmIds))
                        .flatMap(wipeSysRoleMenuByIdsDomainService::deleteByIds)
                        .thenMany(sysMenuRepository.deleteAllById(menuIds)).then());
    }
}
