package com.springddd.domain.role;

import com.springddd.domain.role.exception.RoleStatusNullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleExtendInfoTest {

    @Test
    void constructor_WithValidParams_ShouldCreateSuccessfully() {
        // When
        RoleExtendInfo info = new RoleExtendInfo("Administrator role", true);

        // Then
        assertEquals("Administrator role", info.roleDesc());
        assertTrue(info.roleStatus());
    }

    @Test
    void constructor_WithNullRoleDesc_ShouldCreateSuccessfully() {
        // When
        RoleExtendInfo info = new RoleExtendInfo(null, true);

        // Then
        assertNull(info.roleDesc());
        assertTrue(info.roleStatus());
    }

    @Test
    void constructor_WithNullRoleStatus_ShouldThrowException() {
        // When/Then
        assertThrows(RoleStatusNullException.class, () -> {
            new RoleExtendInfo("Administrator role", null);
        });
    }

    @Test
    void constructor_WithBothNull_ShouldThrowException() {
        // When/Then
        assertThrows(RoleStatusNullException.class, () -> {
            new RoleExtendInfo(null, null);
        });
    }

    @Test
    void equals_ShouldWorkCorrectly() {
        // Given
        RoleExtendInfo info1 = new RoleExtendInfo("Admin", true);
        RoleExtendInfo info2 = new RoleExtendInfo("Admin", true);
        RoleExtendInfo info3 = new RoleExtendInfo("User", true);

        // Then
        assertEquals(info1, info2);
        assertNotEquals(info1, info3);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        // Given
        RoleExtendInfo info1 = new RoleExtendInfo("Admin", true);
        RoleExtendInfo info2 = new RoleExtendInfo("Admin", true);

        // Then
        assertEquals(info1.hashCode(), info2.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectFormat() {
        // Given
        RoleExtendInfo info = new RoleExtendInfo("Admin", true);

        // When
        String result = info.toString();

        // Then
        assertTrue(result.contains("Admin"));
    }
}
