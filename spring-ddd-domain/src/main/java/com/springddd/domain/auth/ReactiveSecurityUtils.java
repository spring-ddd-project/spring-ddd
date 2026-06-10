package com.springddd.domain.auth;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

public final class ReactiveSecurityUtils {

    public static Mono<AuthUser> getCurrentUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(Objects::nonNull)
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

    /**
     * Returns the recommended concurrency level based on available processors.
     * This is actually NOT a blocking call.
     * It's a simple JVM call that returns the number of available processors.
     * It doesn't involve I/O, network, or any blocking operation.
     * It's essentially a constant during the JVM's lifetime (unless the JVM dynamically adjusts it, but the call itself is non-blocking).
     */
    public static Integer concurrency() {
        return Runtime.getRuntime().availableProcessors() * 2;
    }
}
