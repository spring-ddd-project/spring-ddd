package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GenAggregateExtendInfoTest {

    @Test
    void shouldCreateWithTrueValue() {
        GenAggregateExtendInfo info = new GenAggregateExtendInfo(true);
        assertTrue(info.hasCreated());
    }

    @Test
    void shouldCreateWithFalseValue() {
        GenAggregateExtendInfo info = new GenAggregateExtendInfo(false);
        assertFalse(info.hasCreated());
    }

    @Test
    void shouldCreateWithNullValue() {
        GenAggregateExtendInfo info = new GenAggregateExtendInfo(null);
        assertNull(info.hasCreated());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        GenAggregateExtendInfo info1 = new GenAggregateExtendInfo(true);
        GenAggregateExtendInfo info2 = new GenAggregateExtendInfo(true);
        assertEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentValues() {
        GenAggregateExtendInfo info1 = new GenAggregateExtendInfo(true);
        GenAggregateExtendInfo info2 = new GenAggregateExtendInfo(false);
        assertNotEquals(info1, info2);
    }

    @Test
    void toString_shouldReturnValue() {
        GenAggregateExtendInfo info = new GenAggregateExtendInfo(true);
        String result = info.toString();
        assertTrue(result.contains("true"));
    }
}