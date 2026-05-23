package com.springddd.domain.auth;

import com.springddd.domain.role.ColumnRule;
import com.springddd.domain.role.DataPermission;
import com.springddd.domain.user.UserId;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AuthUserTest {

    @Test
    void testGetAuthoritiesWithPermissions() {
        AuthUser user = new AuthUser();
        user.setPermissions(List.of("sys:user:list", "sys:dept:list"));
        var authorities = user.getAuthorities();
        assertThat(authorities).hasSize(2);
        assertThat(authorities).extracting("authority").containsExactlyInAnyOrder("sys:user:list", "sys:dept:list");
    }

    @Test
    void testGetAuthoritiesEmpty() {
        AuthUser user = new AuthUser();
        user.setPermissions(List.of());
        var authorities = user.getAuthorities();
        assertThat(authorities).isEmpty();
    }

    @Test
    void testGetAuthoritiesNull() {
        AuthUser user = new AuthUser();
        user.setPermissions(null);
        var authorities = user.getAuthorities();
        assertThat(authorities).isEmpty();
    }

    @Test
    void testGetAuthoritiesFiltersBlank() {
        AuthUser user = new AuthUser();
        user.setPermissions(List.of("sys:user:list", "", "   "));
        var authorities = user.getAuthorities();
        assertThat(authorities).hasSize(1);
        assertThat(authorities.iterator().next()).isInstanceOf(SimpleGrantedAuthority.class);
    }

    @Test
    void testIsAccountNonLocked() {
        AuthUser user = new AuthUser();
        user.setLockStatus(false);
        assertThat(user.isAccountNonLocked()).isTrue();
        user.setLockStatus(true);
        assertThat(user.isAccountNonLocked()).isFalse();
        user.setLockStatus(null);
        assertThat(user.isAccountNonLocked()).isTrue();
    }

    @Test
    void testIsEnabled() {
        AuthUser user = new AuthUser();
        user.setLockStatus(false);
        assertThat(user.isEnabled()).isTrue();
        user.setLockStatus(true);
        assertThat(user.isEnabled()).isFalse();
        user.setLockStatus(null);
        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    void testIsAccountNonExpired() {
        AuthUser user = new AuthUser();
        assertThat(user.isAccountNonExpired()).isTrue();
    }

    @Test
    void testIsCredentialsNonExpired() {
        AuthUser user = new AuthUser();
        assertThat(user.isCredentialsNonExpired()).isTrue();
    }

    @Test
    void testGetPasswordAndUsername() {
        AuthUser user = new AuthUser();
        user.setPassword("secret");
        user.setUsername("admin");
        assertThat(user.getPassword()).isEqualTo("secret");
        assertThat(user.getUsername()).isEqualTo("admin");
    }

    @Test
    void testDataPermissionField() {
        AuthUser user = new AuthUser();
        DataPermission dp = new DataPermission(null, null, 2, null);
        user.setDataPermission(dp);
        assertThat(user.getDataPermission().dataScope()).isEqualTo(2);
    }

    @Test
    void testColumnPermissions() {
        AuthUser user = new AuthUser();
        user.setColumnPermissions(Map.of("sys_user", Set.of("username", "email")));
        assertThat(user.getColumnPermissions()).containsKey("sys_user");
    }

    @Test
    void testColumnRules() {
        AuthUser user = new AuthUser();
        ColumnRule rule = new ColumnRule();
        rule.setEntityCode("sys_user");
        user.setColumnRules(List.of(rule));
        assertThat(user.getColumnRules()).hasSize(1);
    }
}
