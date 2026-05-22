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
import com.springddd.domain.role.ColumnRule;
import com.springddd.domain.role.DataPermission;
import com.springddd.domain.role.RowScope;
import com.springddd.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

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
                    user.setDeptId(sysUserView.getDeptId());

                    return sysUserRoleQueryService.queryLinkUserAndRole(user.getUserId().value())
                            .flatMapMany(Flux::fromIterable)
                            .map(SysUserRoleView::getRoleId)
                            .distinct()
                            .collectList()
                            .flatMap(roleIds -> Flux.fromIterable(roleIds)
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
                                                                .flatMapMany(Flux::fromIterable)
                                                )
                                                .flatMap(roleMenuView ->
                                                        sysMenuQueryService.queryByMenuId(roleMenuView.getMenuId())
                                                )
                                                .collectList()
                                                .map(menus -> {
                                                    user.setMenuIds(menus.stream().map(SysMenuView::getId).toList());

                                                    List<String> permissions = menus.stream()
                                                            .map(SysMenuView::getPermission)
                                                            .filter(permission -> !ObjectUtils.isEmpty(permission))
                                                            .distinct()
                                                            .collect(Collectors.toList());

                                                    user.setPermissions(permissions);
                                                    return user;
                                                })
                                                .flatMap(u -> {
                                                    Map<String, Set<String>> columnPermissions = mergeColumnPermissions(roleViews);
                                                    u.setColumnPermissions(columnPermissions);

                                                    List<ColumnRule> columnRules = mergeColumnRules(roleViews);
                                                    u.setColumnRules(columnRules);

                                                    DataPermission dataPermission = mergeDataPermission(roleViews);
                                                    u.setDataPermission(dataPermission);

                                                    return Mono.just(u);
                                                });

                                    }));

                });
    }

    private Map<String, Set<String>> mergeColumnPermissions(List<SysRoleView> roleViews) {
        Map<String, Set<String>> result = new HashMap<>();
        if (roleViews == null || roleViews.isEmpty()) {
            return result;
        }

        for (SysRoleView roleView : roleViews) {
            DataPermission dp = roleView.getDataPermission();
            if (dp == null || dp.columnRules() == null || dp.columnRules().isEmpty()) {
                continue;
            }
            for (ColumnRule rule : dp.columnRules()) {
                String entityCode = rule.getEntityCode();
                List<String> columns = rule.getColumns();
                if (entityCode == null || columns == null || columns.isEmpty()) {
                    continue;
                }
                result.computeIfAbsent(entityCode, k -> new HashSet<>()).addAll(columns);
            }
        }

        return result;
    }

    private List<ColumnRule> mergeColumnRules(List<SysRoleView> roleViews) {
        List<ColumnRule> result = new ArrayList<>();
        if (roleViews == null || roleViews.isEmpty()) {
            return result;
        }
        for (SysRoleView roleView : roleViews) {
            DataPermission dp = roleView.getDataPermission();
            if (dp == null || dp.columnRules() == null) {
                continue;
            }
            result.addAll(dp.columnRules());
        }
        return result;
    }

    private DataPermission mergeDataPermission(List<SysRoleView> roleViews) {
        if (roleViews == null || roleViews.isEmpty()) {
            return null;
        }

        Integer strictestDataScope = 1; // default: all
        Set<Long> mergedDeptIds = new HashSet<>();
        Set<Long> mergedPostIds = new HashSet<>();
        Set<Long> mergedUserIds = new HashSet<>();
        boolean self = false;
        List<ColumnRule> mergedColumnRules = new ArrayList<>();

        for (SysRoleView roleView : roleViews) {
            DataPermission dp = roleView.getDataPermission();
            if (dp == null) {
                continue;
            }

            // Merge dataScope: take the strictest (largest number)
            Integer ds = dp.dataScope();
            if (ds != null && ds > strictestDataScope) {
                strictestDataScope = ds;
            }

            // Merge columnRules
            if (dp.columnRules() != null) {
                mergedColumnRules.addAll(dp.columnRules());
            }

            // Merge deptIds from top-level
            if (dp.deptIds() != null) {
                mergedDeptIds.addAll(dp.deptIds());
            }

            // Merge RowScope
            RowScope rs = dp.rowScope();
            if (rs != null) {
                if (rs.deptIds() != null) {
                    mergedDeptIds.addAll(rs.deptIds());
                }
                if (rs.postIds() != null) {
                    mergedPostIds.addAll(rs.postIds());
                }
                if (rs.userIds() != null) {
                    mergedUserIds.addAll(rs.userIds());
                }
                if (Boolean.TRUE.equals(rs.self())) {
                    self = true;
                }
            }
        }

        RowScope mergedRowScope = new RowScope(
                new ArrayList<>(mergedDeptIds),
                new ArrayList<>(mergedPostIds),
                new ArrayList<>(mergedUserIds),
                self
        );

        return new DataPermission(mergedRowScope, mergedColumnRules, strictestDataScope,
                new ArrayList<>(mergedDeptIds));
    }
}
