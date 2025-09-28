package com.springddd.domain.role;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ColumnRuleTest {

    @Test
    void shouldCreateWithDefaultConstructor() {
        ColumnRule columnRule = new ColumnRule();
        assertNotNull(columnRule);
    }

    @Test
    void shouldCreateWithAllArgsConstructor() {
        List<String> columns = Arrays.asList("col1", "col2", "col3");

        ColumnRule columnRule = new ColumnRule("user", "用户表", columns);

        assertEquals("user", columnRule.getEntityCode());
        assertEquals("用户表", columnRule.getEntityName());
        assertEquals(columns, columnRule.getColumns());
    }

    @Test
    void shouldSetAndGetEntityCode() {
        ColumnRule columnRule = new ColumnRule();

        columnRule.setEntityCode("order");

        assertEquals("order", columnRule.getEntityCode());
    }

    @Test
    void shouldSetAndGetEntityName() {
        ColumnRule columnRule = new ColumnRule();

        columnRule.setEntityName("订单表");

        assertEquals("订单表", columnRule.getEntityName());
    }

    @Test
    void shouldSetAndGetColumns() {
        ColumnRule columnRule = new ColumnRule();
        List<String> columns = Arrays.asList("id", "name", "status");

        columnRule.setColumns(columns);

        assertEquals(columns, columnRule.getColumns());
        assertEquals(3, columnRule.getColumns().size());
    }

    @Test
    void shouldHandleNullColumns() {
        ColumnRule columnRule = new ColumnRule();
        columnRule.setColumns(null);
        assertNull(columnRule.getColumns());
    }

    @Test
    void shouldHandleEmptyColumns() {
        ColumnRule columnRule = new ColumnRule();
        columnRule.setColumns(Arrays.asList());
        assertNotNull(columnRule.getColumns());
        assertTrue(columnRule.getColumns().isEmpty());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        ColumnRule cr1 = new ColumnRule("entity", "实体", Arrays.asList("col1", "col2"));
        ColumnRule cr2 = new ColumnRule("entity", "实体", Arrays.asList("col1", "col2"));
        assertEquals(cr1, cr2);
    }

    @Test
    void equals_shouldFailForDifferentEntityCode() {
        ColumnRule cr1 = new ColumnRule("entity1", "实体", Arrays.asList("col1"));
        ColumnRule cr2 = new ColumnRule("entity2", "实体", Arrays.asList("col1"));
        assertNotEquals(cr1, cr2);
    }

    @Test
    void equals_shouldFailForDifferentEntityName() {
        ColumnRule cr1 = new ColumnRule("entity", "实体1", Arrays.asList("col1"));
        ColumnRule cr2 = new ColumnRule("entity", "实体2", Arrays.asList("col1"));
        assertNotEquals(cr1, cr2);
    }

    @Test
    void equals_shouldFailForDifferentColumns() {
        ColumnRule cr1 = new ColumnRule("entity", "实体", Arrays.asList("col1"));
        ColumnRule cr2 = new ColumnRule("entity", "实体", Arrays.asList("col2"));
        assertNotEquals(cr1, cr2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        ColumnRule cr1 = new ColumnRule("entity", "实体", Arrays.asList("col1"));
        ColumnRule cr2 = new ColumnRule("entity", "实体", Arrays.asList("col1"));
        assertEquals(cr1.hashCode(), cr2.hashCode());
    }

    @Test
    void toString_shouldReturnValues() {
        ColumnRule columnRule = new ColumnRule("test_entity", "测试实体", Arrays.asList("c1", "c2"));
        String str = columnRule.toString();
        assertTrue(str.contains("test_entity"));
        assertTrue(str.contains("测试实体"));
    }
}
