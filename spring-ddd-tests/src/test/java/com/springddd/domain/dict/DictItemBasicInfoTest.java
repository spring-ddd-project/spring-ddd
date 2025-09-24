package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictItemLabelNullException;
import com.springddd.domain.dict.exception.DictItemValueNullException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictItemBasicInfoTest {

    @Test
    void shouldCreateWithValidValues() {
        DictItemBasicInfo info = new DictItemBasicInfo(1L, "标签", "值");
        assertEquals(1L, info.dictId());
        assertEquals("标签", info.itemLabel());
        assertEquals("值", info.itemValue());
    }

    @Test
    void shouldThrowWhenLabelIsNull() {
        assertThrows(DictItemLabelNullException.class, () -> new DictItemBasicInfo(1L, null, "值"));
    }

    @Test
    void shouldThrowWhenLabelIsEmpty() {
        assertThrows(DictItemLabelNullException.class, () -> new DictItemBasicInfo(1L, "", "值"));
    }

    @Test
    void shouldThrowWhenValueIsNull() {
        assertThrows(DictItemValueNullException.class, () -> new DictItemBasicInfo(1L, "标签", null));
    }

    @Test
    void shouldThrowWhenValueIsEmpty() {
        assertThrows(DictItemValueNullException.class, () -> new DictItemBasicInfo(1L, "标签", ""));
    }

    @Test
    void equals_shouldWorkForSameValues() {
        DictItemBasicInfo info1 = new DictItemBasicInfo(1L, "标签", "值");
        DictItemBasicInfo info2 = new DictItemBasicInfo(1L, "标签", "值");
        assertEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentLabel() {
        DictItemBasicInfo info1 = new DictItemBasicInfo(1L, "标签1", "值");
        DictItemBasicInfo info2 = new DictItemBasicInfo(1L, "标签2", "值");
        assertNotEquals(info1, info2);
    }

    @Test
    void toString_shouldReturnValues() {
        DictItemBasicInfo info = new DictItemBasicInfo(1L, "标签", "值");
        String str = info.toString();
        assertTrue(str.contains("标签"));
        assertTrue(str.contains("值"));
    }
}
