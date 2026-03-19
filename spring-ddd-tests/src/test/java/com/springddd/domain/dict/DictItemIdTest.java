package com.springddd.domain.dict;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictItemIdTest {

    @Test
    void constructor_withValidValue_shouldCreateSuccessfully() {
        DictItemId itemId = new DictItemId(1L);

        assertEquals(1L, itemId.value());
    }

    @Test
    void constructor_withZeroValue_shouldCreateSuccessfully() {
        DictItemId itemId = new DictItemId(0L);
        assertEquals(0L, itemId.value());
    }

    @Test
    void constructor_withNegativeValue_shouldCreateSuccessfully() {
        DictItemId itemId = new DictItemId(-1L);
        assertEquals(-1L, itemId.value());
    }

    @Test
    void value_shouldReturnCorrectValue() {
        DictItemId itemId = new DictItemId(100L);
        assertEquals(100L, itemId.value());
    }

    @Test
    void equals_forSameValue_shouldBeEqual() {
        DictItemId itemId1 = new DictItemId(1L);
        DictItemId itemId2 = new DictItemId(1L);
        assertEquals(itemId1, itemId2);
    }

    @Test
    void equals_forDifferentValue_shouldNotBeEqual() {
        DictItemId itemId1 = new DictItemId(1L);
        DictItemId itemId2 = new DictItemId(2L);
        assertNotEquals(itemId1, itemId2);
    }

    @Test
    void hashCode_forSameValue_shouldBeEqual() {
        DictItemId itemId1 = new DictItemId(1L);
        DictItemId itemId2 = new DictItemId(1L);
        assertEquals(itemId1.hashCode(), itemId2.hashCode());
    }

    @Test
    void toString_shouldReturnCorrectString() {
        DictItemId itemId = new DictItemId(1L);
        assertEquals("DictItemId[value=1]", itemId.toString());
    }
}
