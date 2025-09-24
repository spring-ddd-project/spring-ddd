package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictDictStatusNullException;
import com.springddd.domain.dict.exception.DictSortOrderNullException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictExtendInfoTest {

    @Test
    void shouldCreateWithValidValues() {
        DictExtendInfo info = new DictExtendInfo(1, true);
        assertEquals(1, info.sortOrder());
        assertTrue(info.dictStatus());
    }

    @Test
    void shouldCreateWithZeroValues() {
        DictExtendInfo info = new DictExtendInfo(0, false);
        assertEquals(0, info.sortOrder());
        assertFalse(info.dictStatus());
    }

    @Test
    void shouldThrowWhenSortOrderIsNull() {
        assertThrows(DictSortOrderNullException.class, () -> new DictExtendInfo(null, true));
    }

    @Test
    void shouldThrowWhenDictStatusIsNull() {
        assertThrows(DictDictStatusNullException.class, () -> new DictExtendInfo(1, null));
    }

    @Test
    void equals_shouldWorkForSameValues() {
        DictExtendInfo info1 = new DictExtendInfo(1, true);
        DictExtendInfo info2 = new DictExtendInfo(1, true);
        assertEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentSortOrder() {
        DictExtendInfo info1 = new DictExtendInfo(1, true);
        DictExtendInfo info2 = new DictExtendInfo(2, true);
        assertNotEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentDictStatus() {
        DictExtendInfo info1 = new DictExtendInfo(1, true);
        DictExtendInfo info2 = new DictExtendInfo(1, false);
        assertNotEquals(info1, info2);
    }

    @Test
    void toString_shouldReturnValues() {
        DictExtendInfo info = new DictExtendInfo(5, true);
        String str = info.toString();
        assertTrue(str.contains("5"));
        assertTrue(str.contains("true"));
    }
}
