package com.springddd.domain.leaf;

import com.springddd.domain.AggregateRootId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeafAllocIdTest {

    @Test
    void shouldCreateLeafAllocIdWithValidValue() {
        LeafAllocId leafAllocId = new LeafAllocId(1L);
        assertEquals(1L, leafAllocId.value());
    }

    @Test
    void shouldImplementAggregateRootId() {
        LeafAllocId leafAllocId = new LeafAllocId(1L);
        assertTrue(AggregateRootId.class.isAssignableFrom(LeafAllocId.class));
        assertEquals(1L, leafAllocId.value());
    }

    @Test
    void shouldAllowNullValue() {
        LeafAllocId leafAllocId = new LeafAllocId(null);
        assertNull(leafAllocId.value());
    }

    @Test
    void equals_shouldWorkForSameValue() {
        LeafAllocId id1 = new LeafAllocId(1L);
        LeafAllocId id2 = new LeafAllocId(1L);
        assertEquals(id1, id2);
    }

    @Test
    void equals_shouldFailForDifferentValue() {
        LeafAllocId id1 = new LeafAllocId(1L);
        LeafAllocId id2 = new LeafAllocId(2L);
        assertNotEquals(id1, id2);
    }

    @Test
    void hashCode_shouldBeConsistentForSameValue() {
        LeafAllocId id1 = new LeafAllocId(1L);
        LeafAllocId id2 = new LeafAllocId(1L);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void toString_shouldContainValue() {
        LeafAllocId leafAllocId = new LeafAllocId(1L);
        assertTrue(leafAllocId.toString().contains("1"));
    }
}
