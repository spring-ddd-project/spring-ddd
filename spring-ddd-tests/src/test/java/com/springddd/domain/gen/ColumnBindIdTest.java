package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColumnBindIdTest {

    @Test
    void shouldCreateColumnBindIdWithValue() {
        ColumnBindId columnBindId = new ColumnBindId(1L);

        assertEquals(1L, columnBindId.value());
    }

    @Test
    void shouldCreateColumnBindIdWithNullValue() {
        ColumnBindId columnBindId = new ColumnBindId(null);

        assertNull(columnBindId.value());
    }

    @Test
    void shouldCreateColumnBindIdWithZeroValue() {
        ColumnBindId columnBindId = new ColumnBindId(0L);

        assertEquals(0L, columnBindId.value());
    }

    @Test
    void shouldCreateColumnBindIdWithNegativeValue() {
        ColumnBindId columnBindId = new ColumnBindId(-75L);

        assertEquals(-75L, columnBindId.value());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        ColumnBindId id1 = new ColumnBindId(1L);
        ColumnBindId id2 = new ColumnBindId(1L);
        ColumnBindId id3 = new ColumnBindId(2L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, id3);
    }

    @Test
    void shouldImplementToString() {
        ColumnBindId columnBindId = new ColumnBindId(123L);

        assertEquals("ColumnBindId[value=123]", columnBindId.toString());
    }
}
