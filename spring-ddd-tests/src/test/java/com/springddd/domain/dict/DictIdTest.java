package com.springddd.domain.dict;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictIdTest {

    @Test
    void shouldCreateDictIdWithValidValue() {
        DictId dictId = new DictId(1L);
        assertEquals(1L, dictId.value());
    }

    @Test
    void shouldCreateDictIdWithZero() {
        DictId dictId = new DictId(0L);
        assertEquals(0L, dictId.value());
    }

    @Test
    void shouldCreateDictIdWithLargeValue() {
        DictId dictId = new DictId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, dictId.value());
    }

    @Test
    void equals_shouldWorkForSameValue() {
        DictId id1 = new DictId(1L);
        DictId id2 = new DictId(1L);
        assertEquals(id1, id2);
    }

    @Test
    void equals_shouldFailForDifferentValue() {
        DictId id1 = new DictId(1L);
        DictId id2 = new DictId(2L);
        assertNotEquals(id1, id2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        DictId id1 = new DictId(1L);
        DictId id2 = new DictId(1L);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        DictId dictId = new DictId(123L);
        assertEquals("123", dictId.toString());
    }
}
