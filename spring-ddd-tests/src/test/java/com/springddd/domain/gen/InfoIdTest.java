package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InfoIdTest {

    @Test
    void shouldCreateInfoIdWithValue() {
        InfoId infoId = new InfoId(1L);

        assertEquals(1L, infoId.value());
    }

    @Test
    void shouldCreateInfoIdWithNullValue() {
        InfoId infoId = new InfoId(null);

        assertNull(infoId.value());
    }

    @Test
    void shouldCreateInfoIdWithZeroValue() {
        InfoId infoId = new InfoId(0L);

        assertEquals(0L, infoId.value());
    }

    @Test
    void shouldCreateInfoIdWithNegativeValue() {
        InfoId infoId = new InfoId(-25L);

        assertEquals(-25L, infoId.value());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        InfoId id1 = new InfoId(1L);
        InfoId id2 = new InfoId(1L);
        InfoId id3 = new InfoId(2L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, id3);
    }

    @Test
    void shouldImplementToString() {
        InfoId infoId = new InfoId(77L);

        assertEquals("InfoId[value=77]", infoId.toString());
    }
}
