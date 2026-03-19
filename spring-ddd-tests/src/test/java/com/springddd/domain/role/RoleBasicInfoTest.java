package com.springddd.domain.role;

import com.springddd.domain.role.exception.RoleCodeNullException;
import com.springddd.domain.role.exception.RoleDataScopeNullException;
import com.springddd.domain.role.exception.RoleNameNullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleBasicInfoTest {

    @Test
    void constructor_WithValidParams_ShouldCreateSuccessfully() {
        // When
        RoleBasicInfo info = new RoleBasicInfo("Admin", "ADMIN", 1, true);

        // Then
        assertEquals("Admin", info.roleName());
        assertEquals("ADMIN", info.roleCode());
        assertEquals(1, info.roleDataScope());
        assertTrue(info.roleOwner());
    }

    @Test
    void constructor_WithNullRoleName_ShouldThrowRoleNameNullException() {
        // When & Then
        RoleNameNullException exception = assertThrows(
            RoleNameNullException.class,
            () -> new RoleBasicInfo(null, "ADMIN", 1, true)
        );
        assertEquals(1101, exception.getCode());
    }

    @Test
    void constructor_WithEmptyRoleName_ShouldThrowRoleNameNullException() {
        // When & Then
        RoleNameNullException exception = assertThrows(
            RoleNameNullException.class,
            () -> new RoleBasicInfo("", "ADMIN", 1, true)
        );
        assertEquals(1101, exception.getCode());
    }

    @Test
    void constructor_WithNullRoleCode_ShouldThrowRoleCodeNullException() {
        // When & Then
        RoleCodeNullException exception = assertThrows(
            RoleCodeNullException.class,
            () -> new RoleBasicInfo("Admin", null, 1, true)
        );
        assertEquals(1100, exception.getCode());
    }

    @Test
    void constructor_WithEmptyRoleCode_ShouldThrowRoleCodeNullException() {
        // When & Then
        RoleCodeNullException exception = assertThrows(
            RoleCodeNullException.class,
            () -> new RoleBasicInfo("Admin", "", 1, true)
        );
        assertEquals(1100, exception.getCode());
    }

    @Test
    void constructor_WithNullRoleDataScope_ShouldThrowRoleDataScopeNullException() {
        // When & Then
        RoleDataScopeNullException exception = assertThrows(
            RoleDataScopeNullException.class,
            () -> new RoleBasicInfo("Admin", "ADMIN", null, true)
        );
        assertEquals(1102, exception.getCode());
    }

    @Test
    void constructor_WithNullRoleOwner_ShouldSucceed() {
        // When
        RoleBasicInfo info = new RoleBasicInfo("Admin", "ADMIN", 1, null);

        // Then
        assertEquals("Admin", info.roleName());
        assertEquals("ADMIN", info.roleCode());
        assertEquals(1, info.roleDataScope());
        assertNull(info.roleOwner());
    }

    @Test
    void equals_ShouldWorkCorrectly() {
        // Given
        RoleBasicInfo info1 = new RoleBasicInfo("Admin", "ADMIN", 1, true);
        RoleBasicInfo info2 = new RoleBasicInfo("Admin", "ADMIN", 1, true);
        RoleBasicInfo info3 = new RoleBasicInfo("User", "USER", 1, true);

        // Then
        assertEquals(info1, info2);
        assertNotEquals(info1, info3);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        // Given
        RoleBasicInfo info1 = new RoleBasicInfo("Admin", "ADMIN", 1, true);
        RoleBasicInfo info2 = new RoleBasicInfo("Admin", "ADMIN", 1, true);

        // Then
        assertEquals(info1.hashCode(), info2.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectFormat() {
        // Given
        RoleBasicInfo info = new RoleBasicInfo("Admin", "ADMIN", 1, true);

        // When
        String result = info.toString();

        // Then
        assertTrue(result.contains("Admin"));
        assertTrue(result.contains("ADMIN"));
    }
}
