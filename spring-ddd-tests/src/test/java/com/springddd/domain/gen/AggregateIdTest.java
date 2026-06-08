package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AggregateIdTest {

    @Test
    void shouldCreateAggregateIdWithValue() {
        AggregateId aggregateId = new AggregateId(1L);

        assertEquals(1L, aggregateId.value());
    }

    @Test
    void shouldCreateAggregateIdWithNullValue() {
        AggregateId aggregateId = new AggregateId(null);

        assertNull(aggregateId.value());
    }

    @Test
    void shouldCreateAggregateIdWithZeroValue() {
        AggregateId aggregateId = new AggregateId(0L);

        assertEquals(0L, aggregateId.value());
    }

    @Test
    void shouldCreateAggregateIdWithNegativeValue() {
        AggregateId aggregateId = new AggregateId(-100L);

        assertEquals(-100L, aggregateId.value());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        AggregateId id1 = new AggregateId(1L);
        AggregateId id2 = new AggregateId(1L);
        AggregateId id3 = new AggregateId(2L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, id3);
    }

    @Test
    void shouldImplementToString() {
        AggregateId aggregateId = new AggregateId(42L);

        assertEquals("AggregateId[value=42]", aggregateId.toString());
    }
}
