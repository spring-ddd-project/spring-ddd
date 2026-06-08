package com.springddd.application.service.auth;

import com.springddd.application.service.menu.SysMenuQueryService;
import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.application.service.role.SysRoleMenuQueryService;
import com.springddd.application.service.role.SysRoleQueryService;
import com.springddd.application.service.role.dto.SysRoleView;
import com.springddd.application.service.user.SysUserQueryService;
import com.springddd.application.service.user.SysUserRoleQueryService;
import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j

@Component
@RequiredArgsConstructor
public class AuthReactiveUserDetailsService implements ReactiveUserDetailsService {

    private final SysUserQueryService sysUserQueryService;

    private final SysUserRoleQueryService sysUserRoleQueryService;

    private final SysRoleMenuQueryService sysRoleMenuQueryService;

    private final SysMenuQueryService sysMenuQueryService;

    private final SysRoleQueryService sysRoleQueryService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return sysUserQueryService.queryUserByUsername(username)
                .flatMap(sysUserView -> {
                    AuthUser user = new AuthUser();
                    user.setUserId(new UserId(sysUserView.getId()));
                    user.setUsername(sysUserView.getUsername());
                    user.setPassword(sysUserView.getPassword());
                    user.setLockStatus(sysUserView.getLockStatus());

                    return sysUserRoleQueryService.queryLinkUserAndRole(user.getUserId().value())
                            .flatMapMany(Flux::fromIterable)
                            .map(SysUserRoleView::getRoleId)
                            .distinct()
                            .collectList()
                            .flatMap(roleIds -> {
                                log.info("\n#findByUsername# userId={}, roleIds={}", user.getUserId().value(), roleIds);
                                return Flux.fromIterable(roleIds)
                                        .flatMap(sysRoleQueryService::getById)
                                        .collectList()
                                        .flatMap(roleViews -> {
                                            List<String> roles = roleViews.stream()
                                                    .map(SysRoleView::getRoleCode)
                                                    .toList();
                                            user.setRoles(roles);

                                            return Flux.fromIterable(roleIds)
                                                    .flatMap(roleId ->
                                                            sysRoleMenuQueryService.queryLinkRoleAndMenus(roleId)
                                                                    .doOnNext(list -> log.info("\n#findByUsername# roleId={}, roleMenus={}", roleId, list.stream().map(com.springddd.application.service.role.dto.SysRoleMenuView::getMenuId).toList()))
                                                                    .flatMapMany(Flux::fromIterable)
                                                    )
                                                    .flatMap(roleMenuView -> {
                                                        log.info("\n#findByUsername# query menuId={}", roleMenuView.getMenuId());
                                                        return sysMenuQueryService.queryByMenuId(roleMenuView.getMenuId())
                                                                .doOnNext(menu -> log.info("\n#findByUsername# found menu id={}", menu.getId()))
                                                                .switchIfEmpty(Mono.defer(() -> {
                                                                    log.warn("\n#findByUsername# menu not found menuId={}", roleMenuView.getMenuId());
                                                                    return Mono.empty();
                                                                }));
                                                    })
                                                    .collectList()
                                                    .map(menus -> {
                                                        log.info("\n#findByUsername# menus size={}, menuIds={}", menus.size(), menus.stream().map(SysMenuView::getId).toList());
                                                        user.setMenuIds(menus.stream().map(SysMenuView::getId).toList());

                                                        List<String> permissions = menus.stream()
                                                                .map(SysMenuView::getPermission)
                                                                .filter(permission -> !ObjectUtils.isEmpty(permission))
                                                                .distinct()
                                                                .collect(Collectors.toList());

                                                        user.setPermissions(permissions);
                                                        return user;
                                                    });

                                        });
                            });

                });
    }


}
