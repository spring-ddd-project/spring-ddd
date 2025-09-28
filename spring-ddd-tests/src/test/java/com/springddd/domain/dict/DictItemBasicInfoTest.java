package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictItemLabelNullException;
import com.springddd.domain.dict.exception.DictItemValueNullException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictItemBasicInfoTest {

    @Test
    void shouldCreateWithValidValues() {
        DictItemBasicInfo info = new DictItemBasicInfo("Label1", 1);
        assertEquals("Label1", info.itemLabel());
        assertEquals(1, info.itemValue());
    }

    @Test
    void shouldThrowWhenLabelIsNull() {
        assertThrows(DictItemLabelNullException.class, () ->
            new DictItemBasicInfo(null, 1));
    }

    @Test
    void shouldThrowWhenLabelIsEmpty() {
        assertThrows(DictItemLabelNullException.class, () ->
            new DictItemBasicInfo("", 1));
    }

    @Test
    void shouldThrowWhenValueIsNull() {
        assertThrows(DictItemValueNullException.class, () ->
            new DictItemBasicInfo("Label1", null));
    }

    @Test
    void equals_shouldWorkForSameValues() {
        DictItemBasicInfo info1 = new DictItemBasicInfo("Label1", 1);
        DictItemBasicInfo info2 = new DictItemBasicInfo("Label1", 1);
        assertEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentValues() {
        DictItemBasicInfo info1 = new DictItemBasicInfo("Label1", 1);
        DictItemBasicInfo info2 = new DictItemBasicInfo("Label2", 2);
        assertNotEquals(info1, info2);
    }

    @Test
    void toString_shouldReturnValues() {
        DictItemBasicInfo info = new DictItemBasicInfo("Test", 123);
        String result = info.toString();
        assertTrue(result.contains("Test"));
        assertTrue(result.contains("123"));
    }
}