package com.springddd.domain.dict;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictIdTest {

    @Test
    void constructor_withValidValue_shouldCreateSuccessfully() {
        DictId dictId = new DictId(1L);

        assertEquals(1L, dictId.value());
    }

    @Test
    void constructor_withZeroValue_shouldCreateSuccessfully() {
        DictId dictId = new DictId(0L);
        assertEquals(0L, dictId.value());
    }

    @Test
    void constructor_withNegativeValue_shouldCreateSuccessfully() {
        DictId dictId = new DictId(-1L);
        assertEquals(-1L, dictId.value());
    }

    @Test
    void value_shouldReturnCorrectValue() {
        DictId dictId = new DictId(100L);
        assertEquals(100L, dictId.value());
    }

    @Test
    void equals_forSameValue_shouldBeEqual() {
        DictId dictId1 = new DictId(1L);
        DictId dictId2 = new DictId(1L);
        assertEquals(dictId1, dictId2);
    }

    @Test
    void equals_forDifferentValue_shouldNotBeEqual() {
        DictId dictId1 = new DictId(1L);
        DictId dictId2 = new DictId(2L);
        assertNotEquals(dictId1, dictId2);
    }

    @Test
    void hashCode_forSameValue_shouldBeEqual() {
        DictId dictId1 = new DictId(1L);
        DictId dictId2 = new DictId(1L);
        assertEquals(dictId1.hashCode(), dictId2.hashCode());
    }

    @Test
    void toString_shouldReturnCorrectString() {
        DictId dictId = new DictId(1L);
        assertEquals("DictId[value=1]", dictId.toString());
    }
}
