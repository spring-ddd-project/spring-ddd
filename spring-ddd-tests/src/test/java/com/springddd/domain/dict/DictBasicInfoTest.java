package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictCodeNullException;
import com.springddd.domain.dict.exception.DictNameNullException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictBasicInfoTest {

    @Test
    void shouldCreateWithValidNameAndCode() {
        DictBasicInfo info = new DictBasicInfo("字典A", "DICT_A");
        assertEquals("字典A", info.dictName());
        assertEquals("DICT_A", info.dictCode());
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThrows(DictNameNullException.class, () -> new DictBasicInfo(null, "DICT_A"));
    }

    @Test
    void shouldThrowWhenNameIsEmpty() {
        assertThrows(DictNameNullException.class, () -> new DictBasicInfo("", "DICT_A"));
    }

    @Test
    void shouldThrowWhenCodeIsNull() {
        assertThrows(DictCodeNullException.class, () -> new DictBasicInfo("字典A", null));
    }

    @Test
    void shouldThrowWhenCodeIsEmpty() {
        assertThrows(DictCodeNullException.class, () -> new DictBasicInfo("字典A", ""));
    }

    @Test
    void equals_shouldWorkForSameValues() {
        DictBasicInfo info1 = new DictBasicInfo("字典A", "DICT_A");
        DictBasicInfo info2 = new DictBasicInfo("字典A", "DICT_A");
        assertEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentValues() {
        DictBasicInfo info1 = new DictBasicInfo("字典A", "DICT_A");
        DictBasicInfo info2 = new DictBasicInfo("字典B", "DICT_B");
        assertNotEquals(info1, info2);
    }

    @Test
    void toString_shouldReturnValues() {
        DictBasicInfo info = new DictBasicInfo("测试字典", "TEST_DICT");
        String result = info.toString();
        assertTrue(result.contains("测试字典"));
        assertTrue(result.contains("TEST_DICT"));
    }
}