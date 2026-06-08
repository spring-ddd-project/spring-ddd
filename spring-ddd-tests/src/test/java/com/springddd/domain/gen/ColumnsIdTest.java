package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColumnsIdTest {

    @Test
    void shouldCreateColumnsIdWithValue() {
        ColumnsId columnsId = new ColumnsId(1L);

        assertEquals(1L, columnsId.value());
    }

    @Test
    void shouldCreateColumnsIdWithNullValue() {
        ColumnsId columnsId = new ColumnsId(null);

        assertNull(columnsId.value());
    }

    @Test
    void shouldCreateColumnsIdWithZeroValue() {
        ColumnsId columnsId = new ColumnsId(0L);

        assertEquals(0L, columnsId.value());
    }

    @Test
    void shouldCreateColumnsIdWithNegativeValue() {
        ColumnsId columnsId = new ColumnsId(-50L);

        assertEquals(-50L, columnsId.value());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        ColumnsId id1 = new ColumnsId(1L);
        ColumnsId id2 = new ColumnsId(1L);
        ColumnsId id3 = new ColumnsId(2L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, id3);
    }

    @Test
    void shouldImplementToString() {
        ColumnsId columnsId = new ColumnsId(99L);

        assertEquals("ColumnsId[value=99]", columnsId.toString());
    }
}
