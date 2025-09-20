package com.springddd.domain.auth;

import com.springddd.domain.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test class for AuthUser domain entity.
 * Target: 95% line coverage
 */
@DisplayName("AuthUser Tests")
class AuthUserTest {

    private AuthUser authUser;
    private UserId userId;
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_PHONE = "13800138000";
    private static final String TEST_AVATAR = "avatar.png";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_SEX = "M";
    private static final String TEST_LOCK_STATUS = "0";
    private static final List<String> TEST_ROLES = List.of("ADMIN", "USER");
    private static final List<String> TEST_PERMISSIONS = List.of("system:user:view", "system:user:edit");
    private static final List<Long> TEST_MENU_IDS = List.of(1L, 2L, 3L);

    @BeforeEach
    void setUp() {
        userId = new UserId(1L);
        authUser = new AuthUser();
        authUser.setUserId(userId);
        authUser.setUsername(TEST_USERNAME);
        authUser.setPassword(TEST_PASSWORD);
        authUser.setPhone(TEST_PHONE);
        authUser.setAvatar(TEST_AVATAR);
        authUser.setEmail(TEST_EMAIL);
        authUser.setSex(TEST_SEX);
        authUser.setLockStatus(TEST_LOCK_STATUS);
        authUser.setRoles(TEST_ROLES);
        authUser.setPermissions(TEST_PERMISSIONS);
        authUser.setMenuIds(TEST_MENU_IDS);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Default constructor should create empty AuthUser")
        void defaultConstructor_ShouldCreateEmptyAuthUser() {
            AuthUser newAuthUser = new AuthUser();

            assertNull(newAuthUser.getUserId());
            assertNull(newAuthUser.getUsername());
            assertNull(newAuthUser.getPassword());
            assertNull(newAuthUser.getPhone());
            assertNull(newAuthUser.getAvatar());
            assertNull(newAuthUser.getEmail());
            assertNull(newAuthUser.getSex());
            assertNull(newAuthUser.getLockStatus());
            assertNull(newAuthUser.getRoles());
            assertNull(newAuthUser.getPermissions());
            assertNull(newAuthUser.getMenuIds());
        }
    }

    @Nested
    @DisplayName("Field Getter and Setter Tests")
    class FieldTests {

        @Test
        @DisplayName("Should set and get userId correctly")
        void setAndGetUserId() {
            assertEquals(userId, authUser.getUserId());
        }

        @Test
        @DisplayName("Should set and get username correctly")
        void setAndGetUsername() {
            assertEquals(TEST_USERNAME, authUser.getUsername());
        }

        @Test
        @DisplayName("Should set and get password correctly")
        void setAndGetPassword() {
            assertEquals(TEST_PASSWORD, authUser.getPassword());
        }

        @Test
        @DisplayName("Should set and get phone correctly")
        void setAndGetPhone() {
            assertEquals(TEST_PHONE, authUser.getPhone());
        }

        @Test
        @DisplayName("Should set and get avatar correctly")
        void setAndGetAvatar() {
            assertEquals(TEST_AVATAR, authUser.getAvatar());
        }

        @Test
        @DisplayName("Should set and get email correctly")
        void setAndGetEmail() {
            assertEquals(TEST_EMAIL, authUser.getEmail());
        }

        @Test
        @DisplayName("Should set and get sex correctly")
        void setAndGetSex() {
            assertEquals(TEST_SEX, authUser.getSex());
        }

        @Test
        @DisplayName("Should set and get lockStatus correctly")
        void setAndGetLockStatus() {
            assertEquals(TEST_LOCK_STATUS, authUser.getLockStatus());
        }

        @Test
        @DisplayName("Should set and get roles correctly")
        void setAndGetRoles() {
            assertEquals(TEST_ROLES, authUser.getRoles());
        }

        @Test
        @DisplayName("Should set and get permissions correctly")
        void setAndGetPermissions() {
            assertEquals(TEST_PERMISSIONS, authUser.getPermissions());
        }

        @Test
        @DisplayName("Should set and get menuIds correctly")
        void setAndGetMenuIds() {
            assertEquals(TEST_MENU_IDS, authUser.getMenuIds());
        }
    }

    @Nested
    @DisplayName("UserDetails Interface Tests")
    class UserDetailsTests {

        @Test
        @DisplayName("getPassword should return the password")
        void getPassword_ShouldReturnPassword() {
            assertEquals(TEST_PASSWORD, authUser.getPassword());
        }

        @Test
        @DisplayName("getUsername should return the username")
        void getUsername_ShouldReturnUsername() {
            assertEquals(TEST_USERNAME, authUser.getUsername());
        }

        @Test
        @DisplayName("isAccountNonExpired should return true")
        void isAccountNonExpired_ShouldReturnTrue() {
            assertTrue(authUser.isAccountNonExpired());
        }

        @Test
        @DisplayName("isAccountNonLocked should return true")
        void isAccountNonLocked_ShouldReturnTrue() {
            assertTrue(authUser.isAccountNonLocked());
        }

        @Test
        @DisplayName("isCredentialsNonExpired should return true")
        void isCredentialsNonExpired_ShouldReturnTrue() {
            assertTrue(authUser.isCredentialsNonExpired());
        }

        @Test
        @DisplayName("isEnabled should return true")
        void isEnabled_ShouldReturnTrue() {
            assertTrue(authUser.isEnabled());
        }
    }

    @Nested
    @DisplayName("getAuthorities Tests")
    class GetAuthoritiesTests {

        @Test
        @DisplayName("getAuthorities should return empty list when permissions is null")
        void getAuthorities_NullPermissions_ReturnsEmptyList() {
            authUser.setPermissions(null);

            Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();

            assertNotNull(authorities);
            assertTrue(authorities.isEmpty());
        }

        @Test
        @DisplayName("getAuthorities should return empty list when permissions is empty")
        void getAuthorities_EmptyPermissions_ReturnsEmptyList() {
            authUser.setPermissions(List.of());

            Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();

            assertNotNull(authorities);
            assertTrue(authorities.isEmpty());
        }

        @Test
        @DisplayName("getAuthorities should return authorities from valid permissions")
        void getAuthorities_ValidPermissions_ReturnsAuthorities() {
            Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();

            assertNotNull(authorities);
            assertEquals(2, authorities.size());
        }

        @Test
        @DisplayName("getAuthorities should filter out blank permissions")
        void getAuthorities_BlankPermissions_FiltersThemOut() {
            // Use ArrayList to allow null values for testing
            List<String> permissionsWithBlanks = new java.util.ArrayList<>();
            permissionsWithBlanks.add("perm1");
            permissionsWithBlanks.add("");
            permissionsWithBlanks.add("  ");
            permissionsWithBlanks.add("perm2");
            permissionsWithBlanks.add(null);
            authUser.setPermissions(permissionsWithBlanks);

            Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();

            assertNotNull(authorities);
            assertEquals(2, authorities.size());
        }

        @Test
        @DisplayName("getAuthorities should create correct authority objects")
        void getAuthorities_CreatesCorrectAuthorityObjects() {
            Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();

            assertTrue(authorities.stream()
                    .anyMatch(a -> a.getAuthority().equals("system:user:view")));
            assertTrue(authorities.stream()
                    .anyMatch(a -> a.getAuthority().equals("system:user:edit")));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null values for all fields")
        void handleNullValues() {
            AuthUser nullAuthUser = new AuthUser();
            nullAuthUser.setUserId(null);
            nullAuthUser.setUsername(null);
            nullAuthUser.setPassword(null);
            nullAuthUser.setPhone(null);
            nullAuthUser.setAvatar(null);
            nullAuthUser.setEmail(null);
            nullAuthUser.setSex(null);
            nullAuthUser.setLockStatus(null);
            nullAuthUser.setRoles(null);
            nullAuthUser.setPermissions(null);
            nullAuthUser.setMenuIds(null);

            assertNull(nullAuthUser.getUserId());
            assertNull(nullAuthUser.getUsername());
            assertNull(nullAuthUser.getPassword());
            assertNull(nullAuthUser.getPhone());
            assertNull(nullAuthUser.getAvatar());
            assertNull(nullAuthUser.getEmail());
            assertNull(nullAuthUser.getSex());
            assertNull(nullAuthUser.getLockStatus());
            assertNull(nullAuthUser.getRoles());
            assertNull(nullAuthUser.getPermissions());
            assertNull(nullAuthUser.getMenuIds());
        }

        @Test
        @DisplayName("Should handle empty string values")
        void handleEmptyStringValues() {
            authUser.setUsername("");
            authUser.setPassword("");
            authUser.setPhone("");
            authUser.setEmail("");

            assertEquals("", authUser.getUsername());
            assertEquals("", authUser.getPassword());
            assertEquals("", authUser.getPhone());
            assertEquals("", authUser.getEmail());
        }

        @Test
        @DisplayName("Should handle large menu IDs list")
        void handleLargeMenuIdsList() {
            List<Long> largeMenuIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);
            authUser.setMenuIds(largeMenuIds);

            assertEquals(10, authUser.getMenuIds().size());
        }
    }
}
