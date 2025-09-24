package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictItemItemStatusNullException;
import com.springddd.domain.dict.exception.DictItemSortOrderNullException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictItemExtendInfoTest {

    @Test
    void shouldCreateWithValidValues() {
        DictItemExtendInfo info = new DictItemExtendInfo(1, true);
        assertEquals(1, info.sortOrder());
        assertTrue(info.itemStatus());
    }

    @Test
    void shouldCreateWithZeroValues() {
        DictItemExtendInfo info = new DictItemExtendInfo(0, false);
        assertEquals(0, info.sortOrder());
        assertFalse(info.itemStatus());
    }

    @Test
    void shouldThrowWhenSortOrderIsNull() {
        assertThrows(DictItemSortOrderNullException.class, () -> new DictItemExtendInfo(null, true));
    }

    @Test
    void shouldThrowWhenItemStatusIsNull() {
        assertThrows(DictItemItemStatusNullException.class, () -> new DictItemExtendInfo(1, null));
    }

    @Test
    void equals_shouldWorkForSameValues() {
        DictItemExtendInfo info1 = new DictItemExtendInfo(1, true);
        DictItemExtendInfo info2 = new DictItemExtendInfo(1, true);
        assertEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentSortOrder() {
        DictItemExtendInfo info1 = new DictItemExtendInfo(1, true);
        DictItemExtendInfo info2 = new DictItemExtendInfo(2, true);
        assertNotEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentItemStatus() {
        DictItemExtendInfo info1 = new DictItemExtendInfo(1, true);
        DictItemExtendInfo info2 = new DictItemExtendInfo(1, false);
        assertNotEquals(info1, info2);
    }

    @Test
    void toString_shouldReturnValues() {
        DictItemExtendInfo info = new DictItemExtendInfo(5, true);
        String str = info.toString();
        assertTrue(str.contains("5"));
        assertTrue(str.contains("true"));
    }
}
