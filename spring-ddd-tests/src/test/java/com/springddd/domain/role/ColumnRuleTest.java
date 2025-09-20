package com.springddd.domain.role;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ColumnRuleTest {

    @Test
    void defaultConstructor_ShouldCreateEmptyColumnRule() {
        // When
        ColumnRule columnRule = new ColumnRule();

        // Then
        assertNull(columnRule.getEntityCode());
        assertNull(columnRule.getEntityName());
        assertNull(columnRule.getColumns());
    }

    @Test
    void allArgsConstructor_ShouldCreateColumnRuleWithValues() {
        // Given
        String entityCode = "sys_user";
        String entityName = "System User";
        List<String> columns = Arrays.asList("id", "name", "email");

        // When
        ColumnRule columnRule = new ColumnRule(entityCode, entityName, columns);

        // Then
        assertEquals(entityCode, columnRule.getEntityCode());
        assertEquals(entityName, columnRule.getEntityName());
        assertEquals(columns, columnRule.getColumns());
    }

    @Test
    void setters_ShouldUpdateValues() {
        // Given
        ColumnRule columnRule = new ColumnRule();
        List<String> columns = Arrays.asList("col1", "col2");

        // When
        columnRule.setEntityCode("entity1");
        columnRule.setEntityName("Entity 1");
        columnRule.setColumns(columns);

        // Then
        assertEquals("entity1", columnRule.getEntityCode());
        assertEquals("Entity 1", columnRule.getEntityName());
        assertEquals(columns, columnRule.getColumns());
    }

    @Test
    void equals_ShouldWorkCorrectly() {
        // Given
        ColumnRule rule1 = new ColumnRule("sys_user", "System User", Arrays.asList("id", "name"));
        ColumnRule rule2 = new ColumnRule("sys_user", "System User", Arrays.asList("id", "name"));
        ColumnRule rule3 = new ColumnRule();

        // Then
        assertEquals(rule1, rule2);
        assertNotEquals(rule1, rule3);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        // Given
        ColumnRule rule1 = new ColumnRule("sys_user", "System User", Arrays.asList("id", "name"));
        ColumnRule rule2 = new ColumnRule("sys_user", "System User", Arrays.asList("id", "name"));

        // Then
        assertEquals(rule1.hashCode(), rule2.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectFormat() {
        // Given
        ColumnRule columnRule = new ColumnRule("sys_user", "System User", Arrays.asList("id", "name"));

        // When
        String result = columnRule.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("sys_user"));
    }
}
