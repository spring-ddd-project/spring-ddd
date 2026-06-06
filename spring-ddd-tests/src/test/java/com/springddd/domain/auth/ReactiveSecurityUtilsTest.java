package com.springddd.domain.auth;

import com.springddd.domain.user.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class ReactiveSecurityUtilsTest {

    private Context withAuthUser(AuthUser user) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        return ReactiveSecurityContextHolder.withAuthentication(auth);
    }

    private AuthUser createUser(Long userId, String username) {
        AuthUser user = new AuthUser();
        user.setUserId(new UserId(userId));
        user.setUsername(username);
        user.setRoles(List.of("ADMIN"));
        user.setPermissions(List.of("sys:user:index"));
        user.setMenuIds(List.of(1L, 2L, 3L));
        return user;
    }

    @Test
    @DisplayName("getCurrentUser 应返回当前认证用户")
    void getCurrentUser_shouldReturnAuthUser() {
        AuthUser expected = createUser(1L, "admin");

        StepVerifier.create(ReactiveSecurityUtils.getCurrentUser().contextWrite(withAuthUser(expected)))
                .assertNext(user -> {
                    assertThat(user.getUserId().value()).isEqualTo(1L);
                    assertThat(user.getUsername()).isEqualTo("admin");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("getCurrentUserId 应返回当前用户ID")
    void getCurrentUserId_shouldReturnUserId() {
        AuthUser user = createUser(42L, "test");

        StepVerifier.create(ReactiveSecurityUtils.getCurrentUserId().contextWrite(withAuthUser(user)))
                .assertNext(id -> assertThat(id).isEqualTo(42L))
                .verifyComplete();
    }

    @Test
    @DisplayName("getCurrentUserRoles 应返回用户角色")
    void getCurrentUserRoles_shouldReturnRoles() {
        AuthUser user = createUser(1L, "test");
        user.setRoles(List.of("ADMIN", "USER"));

        StepVerifier.create(ReactiveSecurityUtils.getCurrentUserRoles().contextWrite(withAuthUser(user)))
                .assertNext(roles -> assertThat(roles).containsExactly("ADMIN", "USER"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getCurrentUserPermissions 应返回用户权限")
    void getCurrentUserPermissions_shouldReturnPermissions() {
        AuthUser user = createUser(1L, "test");
        user.setPermissions(List.of("sys:user:index", "sys:user:create"));

        StepVerifier.create(ReactiveSecurityUtils.getCurrentUserPermissions().contextWrite(withAuthUser(user)))
                .assertNext(perms -> assertThat(perms).containsExactly("sys:user:index", "sys:user:create"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getCurrentUserMenuIds 应返回用户菜单ID")
    void getCurrentUserMenuIds_shouldReturnMenuIds() {
        AuthUser user = createUser(1L, "test");
        user.setMenuIds(List.of(10L, 20L, 30L));

        StepVerifier.create(ReactiveSecurityUtils.getCurrentUserMenuIds().contextWrite(withAuthUser(user)))
                .assertNext(menuIds -> assertThat(menuIds).containsExactly(10L, 20L, 30L))
                .verifyComplete();
    }

    @Test
    @DisplayName("构造函数应可被调用")
    void constructor_shouldBeCallable() {
        ReactiveSecurityUtils utils = new ReactiveSecurityUtils();
        assertThat(utils).isNotNull();
    }
}
