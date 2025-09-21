package com.springddd.domain.role;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataPermissionTest {

    @Test
    void defaultConstructor_ShouldCreateEmptyDataPermission() {
        // When
        DataPermission permission = new DataPermission();

        // Then
        assertNull(permission.getRowScope());
        assertNull(permission.getColumnRules());
    }

    @Test
    void allArgsConstructor_ShouldCreateDataPermissionWithValues() {
        // Given
        RowScope rowScope = new RowScope();
        rowScope.setDeptIds(Arrays.asList(1L, 2L, 3L));
        List<ColumnRule> columnRules = Arrays.asList(
            new ColumnRule("entity1", "Entity 1", Arrays.asList("col1", "col2")),
            new ColumnRule("entity2", "Entity 2", Arrays.asList("col3"))
        );

        // When
        DataPermission permission = new DataPermission(rowScope, columnRules);

        // Then
        assertEquals(rowScope, permission.getRowScope());
        assertEquals(columnRules, permission.getColumnRules());
    }

    @Test
    void setters_ShouldUpdateValues() {
        // Given
        DataPermission permission = new DataPermission();
        RowScope rowScope = new RowScope();
        rowScope.setDeptIds(Arrays.asList(1L, 2L));
        List<ColumnRule> columnRules = Collections.singletonList(
            new ColumnRule("entity1", "Entity 1", Arrays.asList("col1"))
        );

        // When
        permission.setRowScope(rowScope);
        permission.setColumnRules(columnRules);

        // Then
        assertEquals(rowScope, permission.getRowScope());
        assertEquals(columnRules, permission.getColumnRules());
    }

    @Test
    void equals_ShouldWorkCorrectly() {
        // Given
        RowScope rowScope = new RowScope();
        rowScope.setDeptIds(Arrays.asList(1L, 2L));
        List<ColumnRule> columnRules = Arrays.asList(
            new ColumnRule("entity1", "Entity 1", Arrays.asList("col1", "col2"))
        );

        DataPermission permission1 = new DataPermission(rowScope, columnRules);
        DataPermission permission2 = new DataPermission(rowScope, columnRules);
        DataPermission permission3 = new DataPermission();

        // Then
        assertEquals(permission1, permission2);
        assertNotEquals(permission1, permission3);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        // Given
        RowScope rowScope = new RowScope();
        rowScope.setDeptIds(Arrays.asList(1L, 2L));
        List<ColumnRule> columnRules = Arrays.asList(
            new ColumnRule("entity1", "Entity 1", Arrays.asList("col1", "col2"))
        );

        DataPermission permission1 = new DataPermission(rowScope, columnRules);
        DataPermission permission2 = new DataPermission(rowScope, columnRules);

        // Then
        assertEquals(permission1.hashCode(), permission2.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectFormat() {
        // Given
        RowScope rowScope = new RowScope();
        rowScope.setDeptIds(Arrays.asList(1L, 2L));
        DataPermission permission = new DataPermission(rowScope, null);

        // When
        String result = permission.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("RowScope"));
    }
}
