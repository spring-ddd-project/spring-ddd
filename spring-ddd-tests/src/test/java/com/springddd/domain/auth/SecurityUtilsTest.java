package com.springddd.domain.auth;

import com.springddd.domain.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SecurityUtilsTest {

    @BeforeEach
    void setUp() {
        SecurityUtils.setUserId(null);
        SecurityUtils.setUsername(null);
        SecurityUtils.setRoles(null);
        SecurityUtils.setPermissions(null);
        SecurityUtils.setMenuIds(null);
    }

    @Test
    void testSetAuthUserContext() {
        AuthUser user = new AuthUser();
        user.setUserId(new UserId(1L));
        user.setUsername("admin");
        user.setRoles(List.of("ROLE_ADMIN"));
        user.setPermissions(List.of("sys:user:list"));
        user.setMenuIds(List.of(1L, 2L));

        SecurityUtils.setAuthUserContext(user);

        assertThat(SecurityUtils.getUserId()).isEqualTo(1L);
        assertThat(SecurityUtils.getUsername()).isEqualTo("admin");
        assertThat(SecurityUtils.getRoles()).containsExactly("ROLE_ADMIN");
        assertThat(SecurityUtils.getPermissions()).containsExactly("sys:user:list");
        assertThat(SecurityUtils.getMenuIds()).containsExactly(1L, 2L);
    }

    @Test
    void testIndividualSetters() {
        SecurityUtils.setUserId(42L);
        SecurityUtils.setUsername("test");
        SecurityUtils.setRoles(List.of("ROLE_USER"));
        SecurityUtils.setPermissions(List.of("perm1"));
        SecurityUtils.setMenuIds(List.of(10L));

        assertThat(SecurityUtils.getUserId()).isEqualTo(42L);
        assertThat(SecurityUtils.getUsername()).isEqualTo("test");
        assertThat(SecurityUtils.getRoles()).containsExactly("ROLE_USER");
        assertThat(SecurityUtils.getPermissions()).containsExactly("perm1");
        assertThat(SecurityUtils.getMenuIds()).containsExactly(10L);
    }

    @Test
    void testConcurrency() {
        assertThat(SecurityUtils.concurrency()).isPositive();
    }

    @Test
    void testNullValues() {
        SecurityUtils.setUserId(null);
        SecurityUtils.setUsername(null);
        SecurityUtils.setRoles(null);
        SecurityUtils.setPermissions(null);
        SecurityUtils.setMenuIds(null);

        assertThat(SecurityUtils.getUserId()).isNull();
        assertThat(SecurityUtils.getUsername()).isNull();
        assertThat(SecurityUtils.getRoles()).isNull();
        assertThat(SecurityUtils.getPermissions()).isNull();
        assertThat(SecurityUtils.getMenuIds()).isNull();
    }

    @Test
    void testSetAuthUserContextWithNullUserId() {
        AuthUser user = new AuthUser();
        user.setUserId(null);
        user.setUsername("admin");
        user.setRoles(List.of("ROLE_ADMIN"));
        user.setPermissions(List.of("sys:user:list"));
        user.setMenuIds(List.of(1L));

        assertThatThrownBy(() -> SecurityUtils.setAuthUserContext(user))
                .isInstanceOf(NullPointerException.class);
    }
}
