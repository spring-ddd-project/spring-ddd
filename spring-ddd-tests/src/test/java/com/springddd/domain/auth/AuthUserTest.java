package com.springddd.domain.auth;

import com.springddd.domain.user.UserId;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;

class AuthUserTest {

    @Test
    void shouldCreateAuthUserWithDefaultConstructor() {
        AuthUser authUser = new AuthUser();
        assertNull(authUser.getUserId());
        assertNull(authUser.getUsername());
        assertNull(authUser.getPassword());
    }

    @Test
    void shouldSetAndGetUserId() {
        AuthUser authUser = new AuthUser();
        UserId userId = new UserId(1L);
        authUser.setUserId(userId);
        assertEquals(userId, authUser.getUserId());
    }

    @Test
    void shouldSetAndGetUsername() {
        AuthUser authUser = new AuthUser();
        authUser.setUsername("testuser");
        assertEquals("testuser", authUser.getUsername());
    }

    @Test
    void shouldSetAndGetPassword() {
        AuthUser authUser = new AuthUser();
        authUser.setPassword("password123");
        assertEquals("password123", authUser.getPassword());
    }

    @Test
    void shouldSetAndGetRoles() {
        AuthUser authUser = new AuthUser();
        List<String> roles = Arrays.asList("ADMIN", "USER");
        authUser.setRoles(roles);
        assertEquals(roles, authUser.getRoles());
    }

    @Test
    void shouldSetAndGetPermissions() {
        AuthUser authUser = new AuthUser();
        List<String> permissions = Arrays.asList("READ", "WRITE");
        authUser.setPermissions(permissions);
        assertEquals(permissions, authUser.getPermissions());
    }

    @Test
    void shouldSetAndGetMenuIds() {
        AuthUser authUser = new AuthUser();
        List<Long> menuIds = Arrays.asList(1L, 2L, 3L);
        authUser.setMenuIds(menuIds);
        assertEquals(menuIds, authUser.getMenuIds());
    }

    @Test
    void isAccountNonLocked_shouldReturnTrueWhenLockStatusIsNull() {
        AuthUser authUser = new AuthUser();
        authUser.setLockStatus(null);
        assertTrue(authUser.isAccountNonLocked());
    }

    @Test
    void isAccountNonLocked_shouldReturnTrueWhenLockStatusIsFalse() {
        AuthUser authUser = new AuthUser();
        authUser.setLockStatus(false);
        assertTrue(authUser.isAccountNonLocked());
    }

    @Test
    void isAccountNonLocked_shouldReturnFalseWhenLockStatusIsTrue() {
        AuthUser authUser = new AuthUser();
        authUser.setLockStatus(true);
        assertFalse(authUser.isAccountNonLocked());
    }

    @Test
    void isEnabled_shouldReturnTrueWhenLockStatusIsNull() {
        AuthUser authUser = new AuthUser();
        authUser.setLockStatus(null);
        assertTrue(authUser.isEnabled());
    }

    @Test
    void isEnabled_shouldReturnTrueWhenLockStatusIsFalse() {
        AuthUser authUser = new AuthUser();
        authUser.setLockStatus(false);
        assertTrue(authUser.isEnabled());
    }

    @Test
    void isEnabled_shouldReturnFalseWhenLockStatusIsTrue() {
        AuthUser authUser = new AuthUser();
        authUser.setLockStatus(true);
        assertFalse(authUser.isEnabled());
    }

    @Test
    void getAuthorities_shouldReturnEmptyListWhenPermissionsIsNull() {
        AuthUser authUser = new AuthUser();
        authUser.setPermissions(null);
        Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void getAuthorities_shouldReturnEmptyListWhenPermissionsIsEmpty() {
        AuthUser authUser = new AuthUser();
        authUser.setPermissions(Arrays.asList());
        Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void getAuthorities_shouldReturnAuthorities() {
        AuthUser authUser = new AuthUser();
        authUser.setPermissions(Arrays.asList("READ", "WRITE"));
        Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();
        assertNotNull(authorities);
        assertEquals(2, authorities.size());
    }
}