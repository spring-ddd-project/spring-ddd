package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GenColumnsExtendInfoTest {

    @Test
    void shouldCreateWithValidValues() {
        GenColumnsExtendInfo info = new GenColumnsExtendInfo(1L, (byte) 1);
        assertEquals(1L, info.propDictId());
        assertEquals((byte) 1, info.typescriptType());
    }

    @Test
    void shouldCreateWithNullPropDictId() {
        GenColumnsExtendInfo info = new GenColumnsExtendInfo(null, (byte) 1);
        assertNull(info.propDictId());
        assertEquals((byte) 1, info.typescriptType());
    }

    @Test
    void shouldCreateWithNullTypescriptType() {
        GenColumnsExtendInfo info = new GenColumnsExtendInfo(1L, null);
        assertEquals(1L, info.propDictId());
        assertNull(info.typescriptType());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        GenColumnsExtendInfo info1 = new GenColumnsExtendInfo(1L, (byte) 1);
        GenColumnsExtendInfo info2 = new GenColumnsExtendInfo(1L, (byte) 1);
        assertEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentValues() {
        GenColumnsExtendInfo info1 = new GenColumnsExtendInfo(1L, (byte) 1);
        GenColumnsExtendInfo info2 = new GenColumnsExtendInfo(2L, (byte) 2);
        assertNotEquals(info1, info2);
    }

    @Test
    void toString_shouldReturnValues() {
        GenColumnsExtendInfo info = new GenColumnsExtendInfo(1L, (byte) 1);
        String result = info.toString();
        assertTrue(result.contains("1"));
    }
}