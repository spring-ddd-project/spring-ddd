package com.springddd.domain.leaf;

import com.springddd.domain.leaf.exception.MaxIdNullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaxIdTest {

    @Test
    void shouldCreateMaxIdWithValidValue() {
        MaxId maxId = new MaxId(100L);
        assertEquals(100L, maxId.value());
    }

    @Test
    void shouldThrowMaxIdNullExceptionWhenValueIsNull() {
        assertThrows(MaxIdNullException.class, () -> new MaxId(null));
    }

    @Test
    void shouldSetValueToOneWhenValueIsZero() {
        MaxId maxId = new MaxId(0L);
        assertEquals(1L, maxId.value());
    }

    @Test
    void shouldSetValueToOneWhenValueIsNegative() {
        MaxId maxId = new MaxId(-5L);
        assertEquals(1L, maxId.value());
    }

    @Test
    void shouldCreateMaxIdWithValueOne() {
        MaxId maxId = new MaxId(1L);
        assertEquals(1L, maxId.value());
    }

    @Test
    void equals_shouldWorkForSameValue() {
        MaxId maxId1 = new MaxId(100L);
        MaxId maxId2 = new MaxId(100L);
        assertEquals(maxId1, maxId2);
    }

    @Test
    void equals_shouldFailForDifferentValue() {
        MaxId maxId1 = new MaxId(100L);
        MaxId maxId2 = new MaxId(200L);
        assertNotEquals(maxId1, maxId2);
    }

    @Test
    void hashCode_shouldBeConsistentForSameValue() {
        MaxId maxId1 = new MaxId(100L);
        MaxId maxId2 = new MaxId(100L);
        assertEquals(maxId1.hashCode(), maxId2.hashCode());
    }

    @Test
    void toString_shouldContainValue() {
        MaxId maxId = new MaxId(100L);
        assertTrue(maxId.toString().contains("100"));
    }
}
