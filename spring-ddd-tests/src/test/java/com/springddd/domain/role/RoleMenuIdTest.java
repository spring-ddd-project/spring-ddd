package com.springddd.domain.role;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleMenuIdTest {

    @Test
    void constructor_WithValidLongValue_ShouldCreateRoleMenuId() {
        // When
        RoleMenuId roleMenuId = new RoleMenuId(1L);

        // Then
        assertEquals(1L, roleMenuId.value());
    }

    @Test
    void constructor_WithNullValue_ShouldCreateRoleMenuIdWithNull() {
        // When
        RoleMenuId roleMenuId = new RoleMenuId(null);

        // Then
        assertNull(roleMenuId.value());
    }

    @Test
    void equals_ShouldWorkCorrectly() {
        // Given
        RoleMenuId roleMenuId1 = new RoleMenuId(1L);
        RoleMenuId roleMenuId2 = new RoleMenuId(1L);
        RoleMenuId roleMenuId3 = new RoleMenuId(2L);

        // Then
        assertEquals(roleMenuId1, roleMenuId2);
        assertNotEquals(roleMenuId1, roleMenuId3);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        // Given
        RoleMenuId roleMenuId1 = new RoleMenuId(1L);
        RoleMenuId roleMenuId2 = new RoleMenuId(1L);

        // Then
        assertEquals(roleMenuId1.hashCode(), roleMenuId2.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectFormat() {
        // Given
        RoleMenuId roleMenuId = new RoleMenuId(123L);

        // When
        String result = roleMenuId.toString();

        // Then
        assertTrue(result.contains("123"));
    }

    @Test
    void value_ShouldReturnCorrectLongValue() {
        // Given
        Long expectedValue = 999L;
        RoleMenuId roleMenuId = new RoleMenuId(expectedValue);

        // When
        Long actualValue = roleMenuId.value();

        // Then
        assertEquals(expectedValue, actualValue);
    }
}
