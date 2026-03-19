package com.springddd.domain.auth;

import com.springddd.domain.user.UserId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test class for SecurityUtils.
 * Target: 95% line coverage
 */
@DisplayName("SecurityUtils Tests")
class SecurityUtilsTest {

    private AuthUser authUser;
    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_USERNAME = "testuser";
    private static final List<String> TEST_ROLES = List.of("ADMIN", "USER");
    private static final List<String> TEST_PERMISSIONS = List.of("system:user:view", "system:user:edit");
    private static final List<Long> TEST_MENU_IDS = List.of(1L, 2L, 3L);

    @BeforeEach
    void setUp() {
        // Reset static state before each test
        SecurityUtils.setUserId(null);
        SecurityUtils.setUsername(null);
        SecurityUtils.setRoles(null);
        SecurityUtils.setPermissions(null);
        SecurityUtils.setMenuIds(null);

        // Create test AuthUser
        authUser = new AuthUser();
        authUser.setUserId(new UserId(TEST_USER_ID));
        authUser.setUsername(TEST_USERNAME);
        authUser.setPassword("password123");
        authUser.setPhone("13800138000");
        authUser.setAvatar("avatar.png");
        authUser.setEmail("test@example.com");
        authUser.setSex("M");
        authUser.setLockStatus("0");
        authUser.setRoles(TEST_ROLES);
        authUser.setPermissions(TEST_PERMISSIONS);
        authUser.setMenuIds(TEST_MENU_IDS);
    }

    @AfterEach
    void tearDown() {
        // Clean up static state after each test
        SecurityUtils.setUserId(null);
        SecurityUtils.setUsername(null);
        SecurityUtils.setRoles(null);
        SecurityUtils.setPermissions(null);
        SecurityUtils.setMenuIds(null);
    }

    @Nested
    @DisplayName("setAuthUserContext Tests")
    class SetAuthUserContextTests {

        @Test
        @DisplayName("setAuthUserContext should set all context fields from AuthUser")
        void setAuthUserContext_SetsAllFields() {
            SecurityUtils.setAuthUserContext(authUser);

            assertEquals(TEST_USER_ID, SecurityUtils.getUserId());
            assertEquals(TEST_USERNAME, SecurityUtils.getUsername());
            assertEquals(TEST_ROLES, SecurityUtils.getRoles());
            assertEquals(TEST_PERMISSIONS, SecurityUtils.getPermissions());
            assertEquals(TEST_MENU_IDS, SecurityUtils.getMenuIds());
        }
    }

    @Nested
    @DisplayName("UserId Getter and Setter Tests")
    class UserIdTests {

        @Test
        @DisplayName("setUserId should set the userId")
        void setUserId_SetsValue() {
            SecurityUtils.setUserId(100L);

            assertEquals(100L, SecurityUtils.getUserId());
        }

        @Test
        @DisplayName("getUserId should return null when not set")
        void getUserId_ReturnsNullWhenNotSet() {
            assertNull(SecurityUtils.getUserId());
        }

        @Test
        @DisplayName("getUserId should return set value")
        void getUserId_ReturnsSetValue() {
            SecurityUtils.setUserId(42L);

            assertEquals(42L, SecurityUtils.getUserId());
        }
    }

    @Nested
    @DisplayName("Username Getter and Setter Tests")
    class UsernameTests {

        @Test
        @DisplayName("setUsername should set the username")
        void setUsername_SetsValue() {
            SecurityUtils.setUsername("newuser");

            assertEquals("newuser", SecurityUtils.getUsername());
        }

        @Test
        @DisplayName("getUsername should return null when not set")
        void getUsername_ReturnsNullWhenNotSet() {
            assertNull(SecurityUtils.getUsername());
        }

        @Test
        @DisplayName("getUsername should return set value")
        void getUsername_ReturnsSetValue() {
            SecurityUtils.setUsername("testuser");

            assertEquals("testuser", SecurityUtils.getUsername());
        }
    }

    @Nested
    @DisplayName("Roles Getter and Setter Tests")
    class RolesTests {

        @Test
        @DisplayName("setRoles should set the roles")
        void setRoles_SetsValue() {
            List<String> newRoles = List.of("ROLE_ADMIN", "ROLE_USER");
            SecurityUtils.setRoles(newRoles);

            assertEquals(newRoles, SecurityUtils.getRoles());
        }

        @Test
        @DisplayName("getRoles should return null when not set")
        void getRoles_ReturnsNullWhenNotSet() {
            assertNull(SecurityUtils.getRoles());
        }

        @Test
        @DisplayName("getRoles should return set value")
        void getRoles_ReturnsSetValue() {
            List<String> roles = List.of("ADMIN");
            SecurityUtils.setRoles(roles);

            assertEquals(roles, SecurityUtils.getRoles());
        }
    }

    @Nested
    @DisplayName("Permissions Getter and Setter Tests")
    class PermissionsTests {

        @Test
        @DisplayName("setPermissions should set the permissions")
        void setPermissions_SetsValue() {
            List<String> newPermissions = List.of("system:config:edit");
            SecurityUtils.setPermissions(newPermissions);

            assertEquals(newPermissions, SecurityUtils.getPermissions());
        }

        @Test
        @DisplayName("getPermissions should return null when not set")
        void getPermissions_ReturnsNullWhenNotSet() {
            assertNull(SecurityUtils.getPermissions());
        }

        @Test
        @DisplayName("getPermissions should return set value")
        void getPermissions_ReturnsSetValue() {
            List<String> permissions = List.of("system:user:view");
            SecurityUtils.setPermissions(permissions);

            assertEquals(permissions, SecurityUtils.getPermissions());
        }
    }

    @Nested
    @DisplayName("MenuIds Getter and Setter Tests")
    class MenuIdsTests {

        @Test
        @DisplayName("setMenuIds should set the menuIds")
        void setMenuIds_SetsValue() {
            List<Long> newMenuIds = List.of(10L, 20L, 30L);
            SecurityUtils.setMenuIds(newMenuIds);

            assertEquals(newMenuIds, SecurityUtils.getMenuIds());
        }

        @Test
        @DisplayName("getMenuIds should return null when not set")
        void getMenuIds_ReturnsNullWhenNotSet() {
            assertNull(SecurityUtils.getMenuIds());
        }

        @Test
        @DisplayName("getMenuIds should return set value")
        void getMenuIds_ReturnsSetValue() {
            List<Long> menuIds = List.of(5L, 6L);
            SecurityUtils.setMenuIds(menuIds);

            assertEquals(menuIds, SecurityUtils.getMenuIds());
        }
    }

    @Nested
    @DisplayName("concurrency Tests")
    class ConcurrencyTests {

        @Test
        @DisplayName("concurrency should return available processors times 2")
        void concurrency_ReturnsProcessorsTimesTwo() {
            int availableProcessors = Runtime.getRuntime().availableProcessors();
            int expectedConcurrency = availableProcessors * 2;

            int result = SecurityUtils.concurrency();

            assertEquals(expectedConcurrency, result);
        }
    }

    @Nested
    @DisplayName("Context Reset Tests")
    class ContextResetTests {

        @Test
        @DisplayName("Should allow resetting all context values")
        void allowResettingAllContextValues() {
            // Set values
            SecurityUtils.setUserId(1L);
            SecurityUtils.setUsername("user");
            SecurityUtils.setRoles(List.of("ADMIN"));
            SecurityUtils.setPermissions(List.of("perm1"));
            SecurityUtils.setMenuIds(List.of(1L));

            // Reset to null
            SecurityUtils.setUserId(null);
            SecurityUtils.setUsername(null);
            SecurityUtils.setRoles(null);
            SecurityUtils.setPermissions(null);
            SecurityUtils.setMenuIds(null);

            // Verify all are null
            assertNull(SecurityUtils.getUserId());
            assertNull(SecurityUtils.getUsername());
            assertNull(SecurityUtils.getRoles());
            assertNull(SecurityUtils.getPermissions());
            assertNull(SecurityUtils.getMenuIds());
        }
    }

    @Nested
    @DisplayName("Multiple Context Set Tests")
    class MultipleContextSetTests {

        @Test
        @DisplayName("Should handle multiple setAuthUserContext calls")
        void handleMultipleSetAuthUserContextCalls() {
            // First user
            SecurityUtils.setAuthUserContext(authUser);
            assertEquals(TEST_USER_ID, SecurityUtils.getUserId());
            assertEquals(TEST_USERNAME, SecurityUtils.getUsername());

            // Create second user
            AuthUser secondUser = new AuthUser();
            secondUser.setUserId(new UserId(2L));
            secondUser.setUsername("seconduser");
            secondUser.setRoles(List.of("GUEST"));
            secondUser.setPermissions(List.of("system:guest:view"));
            secondUser.setMenuIds(List.of(100L));

            // Second user context
            SecurityUtils.setAuthUserContext(secondUser);
            assertEquals(2L, SecurityUtils.getUserId());
            assertEquals("seconduser", SecurityUtils.getUsername());
            assertEquals(List.of("GUEST"), SecurityUtils.getRoles());
            assertEquals(List.of("system:guest:view"), SecurityUtils.getPermissions());
            assertEquals(List.of(100L), SecurityUtils.getMenuIds());
        }
    }
}
