package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GenColumnBindBasicInfoTest {

    @Test
    void shouldCreateGenColumnBindBasicInfo() {
        GenColumnBindBasicInfo info = new GenColumnBindBasicInfo("type", "entity", (byte) 1, (byte) 1);
        assertEquals("type", info.columnType());
        assertEquals("entity", info.entityType());
        assertEquals((byte) 1, info.componentType());
        assertEquals((byte) 1, info.typescriptType());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        GenColumnBindBasicInfo info1 = new GenColumnBindBasicInfo("type", "entity", (byte) 1, (byte) 1);
        GenColumnBindBasicInfo info2 = new GenColumnBindBasicInfo("type", "entity", (byte) 1, (byte) 1);
        assertEquals(info1, info2);
    }

    @Test
    void toString_shouldReturnValueAsString() {
        GenColumnBindBasicInfo info = new GenColumnBindBasicInfo("type", "entity", (byte) 1, (byte) 1);
        String str = info.toString();
        assertTrue(str.contains("GenColumnBindBasicInfo"));
    }
}
