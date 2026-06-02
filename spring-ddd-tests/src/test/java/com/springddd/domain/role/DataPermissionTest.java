package com.springddd.domain.role;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DataPermissionTest {

    @Test
    void shouldCreateWithDefaultConstructor() {
        DataPermission dataPermission = new DataPermission();
        assertNotNull(dataPermission);
    }

    @Test
    void shouldCreateWithAllArgsConstructor() {
        RowScope rowScope = new RowScope();
        List<ColumnRule> columnRules = Arrays.asList(new ColumnRule("entity1", "实体1", Arrays.asList("col1", "col2")));

        DataPermission dataPermission = new DataPermission(rowScope, columnRules);

        assertEquals(rowScope, dataPermission.getRowScope());
        assertEquals(columnRules, dataPermission.getColumnRules());
    }

    @Test
    void shouldSetAndGetRowScope() {
        DataPermission dataPermission = new DataPermission();
        RowScope rowScope = new RowScope();
        rowScope.setSelf(true);

        dataPermission.setRowScope(rowScope);

        assertEquals(rowScope, dataPermission.getRowScope());
        assertTrue(dataPermission.getRowScope().getSelf());
    }

    @Test
    void shouldSetAndGetColumnRules() {
        DataPermission dataPermission = new DataPermission();
        List<ColumnRule> columnRules = Arrays.asList(
            new ColumnRule("user", "用户表", Arrays.asList("name", "email"))
        );

        dataPermission.setColumnRules(columnRules);

        assertEquals(columnRules, dataPermission.getColumnRules());
        assertEquals(1, dataPermission.getColumnRules().size());
    }

    @Test
    void shouldHandleNullRowScope() {
        DataPermission dataPermission = new DataPermission();
        dataPermission.setRowScope(null);
        assertNull(dataPermission.getRowScope());
    }

    @Test
    void shouldHandleNullColumnRules() {
        DataPermission dataPermission = new DataPermission();
        dataPermission.setColumnRules(null);
        assertNull(dataPermission.getColumnRules());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        RowScope rowScope = new RowScope();
        List<ColumnRule> columnRules = Arrays.asList(new ColumnRule("e", "n", Arrays.asList("c")));
        DataPermission dp1 = new DataPermission(rowScope, columnRules);
        DataPermission dp2 = new DataPermission(rowScope, columnRules);
        assertEquals(dp1, dp2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        RowScope rowScope = new RowScope();
        List<ColumnRule> columnRules = Arrays.asList(new ColumnRule("e", "n", Arrays.asList("c")));
        DataPermission dp1 = new DataPermission(rowScope, columnRules);
        DataPermission dp2 = new DataPermission(rowScope, columnRules);
        assertEquals(dp1.hashCode(), dp2.hashCode());
    }
}
