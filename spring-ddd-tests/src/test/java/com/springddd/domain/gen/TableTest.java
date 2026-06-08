package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    @Test
    void shouldCreateTableWithValidValues() {
        Table table = new Table(true, false, true, (byte) 1, (byte) 0);
        assertTrue(table.tableVisible());
        assertFalse(table.tableOrder());
        assertTrue(table.tableFilter());
        assertEquals((byte) 1, table.tableFilterComponent());
        assertEquals((byte) 0, table.tableFilterType());
    }

    @Test
    void shouldCreateTableWithoutFilterComponent() {
        Table table = new Table(true, false, false, (byte) 0, (byte) 0);
        assertTrue(table.tableVisible());
        assertFalse(table.tableOrder());
        assertFalse(table.tableFilter());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        Table table1 = new Table(true, false, true, (byte) 1, (byte) 0);
        Table table2 = new Table(true, false, true, (byte) 1, (byte) 0);
        assertEquals(table1, table2);
    }

    @Test
    void toString_shouldReturnValueAsString() {
        Table table = new Table(true, false, true, (byte) 1, (byte) 0);
        String str = table.toString();
        assertTrue(str.contains("Table"));
    }

    @Test
    void shouldThrowWhenVisibleIsNull() {
        assertThrows(VisibleNullException.class, () ->
            new Table(null, false, false, (byte) 0, (byte) 0));
    }

    @Test
    void shouldThrowWhenOrderIsNull() {
        assertThrows(OrderNullException.class, () ->
            new Table(true, null, false, (byte) 0, (byte) 0));
    }

    @Test
    void shouldThrowWhenFilterIsNull() {
        assertThrows(FilterNullException.class, () ->
            new Table(true, false, null, (byte) 0, (byte) 0));
    }

    @Test
    void shouldThrowWhenFilterIsTrueButComponentIsNull() {
        assertThrows(FilterComponentNullException.class, () ->
            new Table(true, false, true, null, (byte) 0));
    }
}
