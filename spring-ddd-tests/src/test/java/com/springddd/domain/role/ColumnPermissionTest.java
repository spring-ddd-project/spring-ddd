package com.springddd.domain.role;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ColumnPermissionTest {

    @Test
    void constructor_WithValidParams_ShouldCreateSuccessfully() {
        // Given
        String entityCode = "sys_user";
        String entityName = "System User";
        List<String> columns = Arrays.asList("id", "name", "email");

        // When
        ColumnPermission permission = new ColumnPermission(entityCode, entityName, columns);

        // Then
        assertEquals(entityCode, permission.entityCode());
        assertEquals(entityName, permission.entityName());
        assertEquals(columns, permission.columns());
    }

    @Test
    void constructor_WithNullColumns_ShouldCreateSuccessfully() {
        // Given
        String entityCode = "sys_user";
        String entityName = "System User";

        // When
        ColumnPermission permission = new ColumnPermission(entityCode, entityName, null);

        // Then
        assertEquals(entityCode, permission.entityCode());
        assertEquals(entityName, permission.entityName());
        assertNull(permission.columns());
    }

    @Test
    void equals_ShouldWorkCorrectly() {
        // Given
        ColumnPermission permission1 = new ColumnPermission("sys_user", "System User", Arrays.asList("id", "name"));
        ColumnPermission permission2 = new ColumnPermission("sys_user", "System User", Arrays.asList("id", "name"));
        ColumnPermission permission3 = new ColumnPermission("sys_role", "System Role", Arrays.asList("id", "name"));

        // Then
        assertEquals(permission1, permission2);
        assertNotEquals(permission1, permission3);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        // Given
        ColumnPermission permission1 = new ColumnPermission("sys_user", "System User", Arrays.asList("id", "name"));
        ColumnPermission permission2 = new ColumnPermission("sys_user", "System User", Arrays.asList("id", "name"));

        // Then
        assertEquals(permission1.hashCode(), permission2.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectFormat() {
        // Given
        ColumnPermission permission = new ColumnPermission("sys_user", "System User", Arrays.asList("id", "name"));

        // When
        String result = permission.toString();

        // Then
        assertTrue(result.contains("sys_user"));
        assertTrue(result.contains("System User"));
    }
}
