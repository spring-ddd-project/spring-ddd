package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictItemItemStatusNullException;
import com.springddd.domain.dict.exception.DictItemSortOrderNullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictItemExtendInfoTest {

    @Test
    void constructor_withValidParams_shouldCreateSuccessfully() {
        DictItemExtendInfo info = new DictItemExtendInfo(1, true);

        assertEquals(1, info.sortOrder());
        assertTrue(info.itemStatus());
    }

    @Test
    void constructor_withNullSortOrder_shouldThrowDictItemSortOrderNullException() {
        assertThrows(DictItemSortOrderNullException.class, () -> new DictItemExtendInfo(null, true));
    }

    @Test
    void constructor_withNullItemStatus_shouldThrowDictItemItemStatusNullException() {
        assertThrows(DictItemItemStatusNullException.class, () -> new DictItemExtendInfo(1, null));
    }

    @Test
    void constructor_withBothNull_shouldThrowDictItemSortOrderNullExceptionFirst() {
        assertThrows(DictItemSortOrderNullException.class, () -> new DictItemExtendInfo(null, null));
    }

    @Test
    void constructor_withZeroSortOrder_shouldBeValid() {
        DictItemExtendInfo info = new DictItemExtendInfo(0, false);
        assertEquals(0, info.sortOrder());
        assertFalse(info.itemStatus());
    }

    @Test
    void constructor_withNegativeSortOrder_shouldBeValid() {
        DictItemExtendInfo info = new DictItemExtendInfo(-1, true);
        assertEquals(-1, info.sortOrder());
        assertTrue(info.itemStatus());
    }
}
