package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictCodeNullException;
import com.springddd.domain.dict.exception.DictNameNullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictBasicInfoTest {

    @Test
    void constructor_withValidParams_shouldCreateSuccessfully() {
        DictBasicInfo info = new DictBasicInfo("testDict", "TEST_DICT");

        assertEquals("testDict", info.dictName());
        assertEquals("TEST_DICT", info.dictCode());
    }

    @Test
    void constructor_withNullDictCode_shouldThrowDictCodeNullException() {
        assertThrows(DictCodeNullException.class, () -> new DictBasicInfo("testDict", null));
    }

    @Test
    void constructor_withEmptyDictCode_shouldThrowDictCodeNullException() {
        assertThrows(DictCodeNullException.class, () -> new DictBasicInfo("testDict", ""));
    }

    @Test
    void constructor_withNullDictName_shouldThrowDictNameNullException() {
        assertThrows(DictNameNullException.class, () -> new DictBasicInfo(null, "TEST_DICT"));
    }

    @Test
    void constructor_withEmptyDictName_shouldThrowDictNameNullException() {
        assertThrows(DictNameNullException.class, () -> new DictBasicInfo("", "TEST_DICT"));
    }

    @Test
    void constructor_withBothNull_shouldThrowDictCodeNullExceptionFirst() {
        assertThrows(DictCodeNullException.class, () -> new DictBasicInfo(null, null));
    }
}
