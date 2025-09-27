package com.springddd.domain.role;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ColumnPermissionTest {

    @Test
    void shouldCreateWithValidValues() {
        List<String> columns = Arrays.asList("id", "name", "email");

        ColumnPermission permission = new ColumnPermission("user", "用户", columns);

        assertEquals("user", permission.entityCode());
        assertEquals("用户", permission.entityName());
        assertEquals(columns, permission.columns());
    }

    @Test
    void shouldCreateWithNullColumns() {
        ColumnPermission permission = new ColumnPermission("user", "用户", null);
        assertNull(permission.columns());
    }

    @Test
    void shouldCreateWithEmptyColumns() {
        ColumnPermission permission = new ColumnPermission("user", "用户", Arrays.asList());
        assertNotNull(permission.columns());
        assertTrue(permission.columns().isEmpty());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        ColumnPermission cp1 = new ColumnPermission("user", "用户", Arrays.asList("id", "name"));
        ColumnPermission cp2 = new ColumnPermission("user", "用户", Arrays.asList("id", "name"));
        assertEquals(cp1, cp2);
    }

    @Test
    void equals_shouldFailForDifferentEntityCode() {
        ColumnPermission cp1 = new ColumnPermission("user1", "用户", Arrays.asList("id"));
        ColumnPermission cp2 = new ColumnPermission("user2", "用户", Arrays.asList("id"));
        assertNotEquals(cp1, cp2);
    }

    @Test
    void equals_shouldFailForDifferentEntityName() {
        ColumnPermission cp1 = new ColumnPermission("user", "用户1", Arrays.asList("id"));
        ColumnPermission cp2 = new ColumnPermission("user", "用户2", Arrays.asList("id"));
        assertNotEquals(cp1, cp2);
    }

    @Test
    void equals_shouldFailForDifferentColumns() {
        ColumnPermission cp1 = new ColumnPermission("user", "用户", Arrays.asList("id"));
        ColumnPermission cp2 = new ColumnPermission("user", "用户", Arrays.asList("name"));
        assertNotEquals(cp1, cp2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        ColumnPermission cp1 = new ColumnPermission("user", "用户", Arrays.asList("id"));
        ColumnPermission cp2 = new ColumnPermission("user", "用户", Arrays.asList("id"));
        assertEquals(cp1.hashCode(), cp2.hashCode());
    }

    @Test
    void toString_shouldReturnValues() {
        ColumnPermission permission = new ColumnPermission("test_entity", "测试", Arrays.asList("c1"));
        String str = permission.toString();
        assertTrue(str.contains("test_entity"));
        assertTrue(str.contains("测试"));
    }
}
