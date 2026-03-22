package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictItemLabelNullException;
import com.springddd.domain.dict.exception.DictItemValueNullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictItemBasicInfoTest {

    @Test
    void constructor_withValidParams_shouldCreateSuccessfully() {
        DictItemBasicInfo info = new DictItemBasicInfo("label", 1);

        assertEquals("label", info.itemLabel());
        assertEquals(1, info.itemValue());
    }

    @Test
    void constructor_withNullItemLabel_shouldThrowDictItemLabelNullException() {
        assertThrows(DictItemLabelNullException.class, () -> new DictItemBasicInfo(null, 1));
    }

    @Test
    void constructor_withEmptyItemLabel_shouldThrowDictItemLabelNullException() {
        assertThrows(DictItemLabelNullException.class, () -> new DictItemBasicInfo("", 1));
    }

    @Test
    void constructor_withNullItemValue_shouldThrowDictItemValueNullException() {
        assertThrows(DictItemValueNullException.class, () -> new DictItemBasicInfo("label", null));
    }

    @Test
    void constructor_withBothNull_shouldThrowDictItemLabelNullExceptionFirst() {
        assertThrows(DictItemLabelNullException.class, () -> new DictItemBasicInfo(null, null));
    }

    @Test
    void constructor_withZeroValue_shouldBeValid() {
        DictItemBasicInfo info = new DictItemBasicInfo("label", 0);
        assertEquals(0, info.itemValue());
    }

    @Test
    void constructor_withNegativeValue_shouldBeValid() {
        DictItemBasicInfo info = new DictItemBasicInfo("label", -1);
        assertEquals(-1, info.itemValue());
    }

    @Test
    void constructor_withBlankItemLabel_shouldBeValid() {
        // ObjectUtils.isEmpty only checks null or empty string, not whitespace
        DictItemBasicInfo info = new DictItemBasicInfo("   ", 1);
        assertEquals("   ", info.itemLabel());
    }

    @Test
    void constructor_withWhitespaceOnlyItemLabel_shouldBeValid() {
        DictItemBasicInfo info = new DictItemBasicInfo("\t\n", 1);
        assertEquals("\t\n", info.itemLabel());
    }
}
