package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictCodeNullException;
import com.springddd.domain.dict.exception.DictNameNullException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictBasicInfoTest {

    @Test
    void shouldCreateWithValidValues() {
        DictBasicInfo info = new DictBasicInfo("dictCode", "字典名称");
        assertEquals("dictCode", info.dictCode());
        assertEquals("字典名称", info.dictName());
    }

    @Test
    void shouldThrowWhenCodeIsNull() {
        assertThrows(DictCodeNullException.class, () -> new DictBasicInfo(null, "名称"));
    }

    @Test
    void shouldThrowWhenCodeIsEmpty() {
        assertThrows(DictCodeNullException.class, () -> new DictBasicInfo("", "名称"));
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThrows(DictNameNullException.class, () -> new DictBasicInfo("code", null));
    }

    @Test
    void shouldThrowWhenNameIsEmpty() {
        assertThrows(DictNameNullException.class, () -> new DictBasicInfo("code", ""));
    }

    @Test
    void equals_shouldWorkForSameValues() {
        DictBasicInfo info1 = new DictBasicInfo("code", "名称");
        DictBasicInfo info2 = new DictBasicInfo("code", "名称");
        assertEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentCode() {
        DictBasicInfo info1 = new DictBasicInfo("code1", "名称");
        DictBasicInfo info2 = new DictBasicInfo("code2", "名称");
        assertNotEquals(info1, info2);
    }

    @Test
    void toString_shouldReturnValues() {
        DictBasicInfo info = new DictBasicInfo("code", "名称");
        String str = info.toString();
        assertTrue(str.contains("code"));
        assertTrue(str.contains("名称"));
    }
}
