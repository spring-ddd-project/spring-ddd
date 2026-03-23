package com.springddd.domain.role;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleIdTest {

    @Test
    void constructor_WithValidLongValue_ShouldCreateRoleId() {
        // When
        RoleId roleId = new RoleId(1L);

        // Then
        assertEquals(1L, roleId.value());
    }

    @Test
    void constructor_WithNullValue_ShouldCreateRoleIdWithNull() {
        // When
        RoleId roleId = new RoleId(null);

        // Then
        assertNull(roleId.value());
    }

    @Test
    void equals_ShouldWorkCorrectly() {
        // Given
        RoleId roleId1 = new RoleId(1L);
        RoleId roleId2 = new RoleId(1L);
        RoleId roleId3 = new RoleId(2L);

        // Then
        assertEquals(roleId1, roleId2);
        assertNotEquals(roleId1, roleId3);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        // Given
        RoleId roleId1 = new RoleId(1L);
        RoleId roleId2 = new RoleId(1L);

        // Then
        assertEquals(roleId1.hashCode(), roleId2.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectFormat() {
        // Given
        RoleId roleId = new RoleId(123L);

        // When
        String result = roleId.toString();

        // Then
        assertTrue(result.contains("123"));
    }

    @Test
    void value_ShouldReturnCorrectLongValue() {
        // Given
        Long expectedValue = 999L;
        RoleId roleId = new RoleId(expectedValue);

        // When
        Long actualValue = roleId.value();

        // Then
        assertEquals(expectedValue, actualValue);
    }
}
