package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictDictStatusNullException;
import com.springddd.domain.dict.exception.DictSortOrderNullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictExtendInfoTest {

    @Test
    void constructor_withValidParams_shouldCreateSuccessfully() {
        DictExtendInfo info = new DictExtendInfo(1, true);

        assertEquals(1, info.sortOrder());
        assertTrue(info.dictStatus());
    }

    @Test
    void constructor_withNullSortOrder_shouldThrowDictSortOrderNullException() {
        assertThrows(DictSortOrderNullException.class, () -> new DictExtendInfo(null, true));
    }

    @Test
    void constructor_withNullDictStatus_shouldThrowDictDictStatusNullException() {
        assertThrows(DictDictStatusNullException.class, () -> new DictExtendInfo(1, null));
    }

    @Test
    void constructor_withBothNull_shouldThrowDictSortOrderNullExceptionFirst() {
        assertThrows(DictSortOrderNullException.class, () -> new DictExtendInfo(null, null));
    }

    @Test
    void constructor_withZeroSortOrder_shouldBeValid() {
        DictExtendInfo info = new DictExtendInfo(0, false);
        assertEquals(0, info.sortOrder());
        assertFalse(info.dictStatus());
    }

    @Test
    void constructor_withNegativeSortOrder_shouldBeValid() {
        DictExtendInfo info = new DictExtendInfo(-1, true);
        assertEquals(-1, info.sortOrder());
        assertTrue(info.dictStatus());
    }
}
