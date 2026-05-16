package com.springddd.domain.auth;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
                    Map<String, Set<String>> perms = user.getColumnPermissions();
                    return perms != null ? perms.getOrDefault(entityCode, Collections.emptySet()) : Collections.emptySet();
                });
    }
}
