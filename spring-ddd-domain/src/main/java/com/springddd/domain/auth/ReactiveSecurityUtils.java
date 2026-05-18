package com.springddd.domain.auth;

import com.springddd.domain.role.ColumnRule;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class ReactiveSecurityUtils {

    public static Mono<AuthUser> getCurrentUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .cast(AuthUser.class)
                .switchIfEmpty(Mono.error(new AccessDeniedException("No authenticated user")));
    }

    public static Mono<Long> getCurrentUserId() {
        return getCurrentUser().map(u -> u.getUserId().value());
    }

    public static Mono<List<String>> getCurrentUserRoles() {
        return getCurrentUser().map(AuthUser::getRoles);
    }

    public static Mono<List<String>> getCurrentUserPermissions() {
        return getCurrentUser().map(AuthUser::getPermissions);
    }

    public static Mono<List<Long>> getCurrentUserMenuIds() {
        return getCurrentUser().map(AuthUser::getMenuIds);
    }

    public static Mono<Set<String>> getVisibleColumns(String entityCode) {
        return getCurrentUser()
                .map(user -> {
                    List<ColumnRule> rules = user.getColumnRules();
                    if (rules != null && !rules.isEmpty()) {
                        return resolveVisibleColumns(entityCode, rules, user);
                    }
                    // 回退到旧的预计算映射（向后兼容）
                    Map<String, Set<String>> perms = user.getColumnPermissions();
                    return perms != null ? perms.getOrDefault(entityCode, Collections.emptySet()) : Collections.emptySet();
                });
    }

    private static Set<String> resolveVisibleColumns(String entityCode, List<ColumnRule> rules, AuthUser user) {
        List<ColumnRule> matched = rules.stream()
                .filter(r -> entityCode.equals(r.getEntityCode()))
                .toList();

        if (matched.isEmpty()) {
            return Collections.emptySet();
        }

        Long uid = user.getUserId() != null ? user.getUserId().value() : null;
        Long deptId = user.getDeptId();

        // 优先级 1: 指定用户 (user)
        Set<String> userCols = matched.stream()
                .filter(r -> "user".equals(r.getDimensionType()))
                .filter(r -> r.getDimensionIds() != null && r.getDimensionIds().contains(uid))
                .flatMap(r -> r.getColumns() != null ? r.getColumns().stream() : java.util.stream.Stream.empty())
                .collect(Collectors.toSet());
        if (!userCols.isEmpty()) {
            return userCols;
        }

        // 优先级 2: 指定部门 (dept)
        Set<String> deptCols = matched.stream()
                .filter(r -> "dept".equals(r.getDimensionType()))
                .filter(r -> r.getDimensionIds() != null && r.getDimensionIds().contains(deptId))
                .flatMap(r -> r.getColumns() != null ? r.getColumns().stream() : java.util.stream.Stream.empty())
                .collect(Collectors.toSet());
        if (!deptCols.isEmpty()) {
            return deptCols;
        }

        // 优先级 3: 本人 (self)
        Set<String> selfCols = matched.stream()
                .filter(r -> "self".equals(r.getDimensionType()))
                .flatMap(r -> r.getColumns() != null ? r.getColumns().stream() : java.util.stream.Stream.empty())
                .collect(Collectors.toSet());
        if (!selfCols.isEmpty()) {
            return selfCols;
        }

        // 优先级 4: 全部 (all) 或旧数据兼容（dimensionType 为 null）
        Set<String> allCols = matched.stream()
                .filter(r -> r.getDimensionType() == null || "all".equals(r.getDimensionType()))
                .flatMap(r -> r.getColumns() != null ? r.getColumns().stream() : java.util.stream.Stream.empty())
                .collect(Collectors.toSet());

        return allCols;
    }
}
