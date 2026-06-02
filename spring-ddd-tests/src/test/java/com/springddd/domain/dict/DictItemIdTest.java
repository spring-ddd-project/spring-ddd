package com.springddd.domain.dict;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictItemIdTest {

    @Test
    void shouldCreateDictItemIdWithValidValue() {
        DictItemId dictItemId = new DictItemId(1L);
        assertEquals(1L, dictItemId.value());
    }

    @Test
    void shouldCreateDictItemIdWithZero() {
        DictItemId dictItemId = new DictItemId(0L);
        assertEquals(0L, dictItemId.value());
    }

    @Test
    void equals_shouldWorkForSameValue() {
        DictItemId id1 = new DictItemId(1L);
        DictItemId id2 = new DictItemId(1L);
        assertEquals(id1, id2);
    }

    @Test
    void equals_shouldFailForDifferentValue() {
        DictItemId id1 = new DictItemId(1L);
        DictItemId id2 = new DictItemId(2L);
        assertNotEquals(id1, id2);
    }

    @Test
    void toString_shouldReturnValueAsString() {
        DictItemId dictItemId = new DictItemId(456L);
        assertEquals("DictItemId[value=456]", dictItemId.toString());
    }
}