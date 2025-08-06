package com.springddd.application.service.auth;

import com.springddd.application.service.menu.SysMenuQueryService;
import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.application.service.role.SysRoleMenuQueryService;
import com.springddd.application.service.user.SysUserQueryService;
import com.springddd.application.service.user.SysUserRoleQueryService;
import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.menu.MenuPermission;
import com.springddd.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthUserDetailsService implements ReactiveUserDetailsService {

    private final SysUserQueryService sysUserQueryService;

    private final SysUserRoleQueryService sysUserRoleQueryService;

    private final SysRoleMenuQueryService sysRoleMenuQueryService;

    private final SysMenuQueryService sysMenuQueryService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return sysUserQueryService.queryUserByUsername(username)
                .flatMap(sysUserView -> {
                    AuthUser user = new AuthUser();
                    user.setUserId(new UserId(sysUserView.getId()));
                    user.setUsername(sysUserView.getUsername());
                    user.setPassword(sysUserView.getPassword());

                    return sysUserRoleQueryService.queryLinkUserAndRole(user.getUserId().value())
                            .flatMapMany(Flux::fromIterable)
                            .flatMap(roleView ->
                                    sysRoleMenuQueryService.queryLinkRoleAndMenus(roleView.getRoleId())
                                            .flatMapMany(Flux::fromIterable)
                            )
                            .flatMap(roleMenuView ->
                                    sysMenuQueryService.queryByMenuId(roleMenuView.getMenuId())
                            )
                            .map(SysMenuView::getPermission)
                            .distinct()
                            .map(MenuPermission::new)
                            .collectList()
                            .map(permissions -> {
                                user.setPermissions(permissions);
                                return user;
                            });
                });
    }

}
