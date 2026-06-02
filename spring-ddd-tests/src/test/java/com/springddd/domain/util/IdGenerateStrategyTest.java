package com.springddd.domain.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdGenerateStrategyTest {

    @Test
    void shouldContainAllEnumValues() {
        IdGenerateStrategy[] values = IdGenerateStrategy.values();
        assertEquals(2, values.length);
    }

    @Test
    void shouldHaveIdTempValue() {
        assertNotNull(IdGenerateStrategy.ID_TEMP);
        assertEquals("ID_TEMP", IdGenerateStrategy.ID_TEMP.name());
    }

    @Test
    void shouldHaveLeafSegmentValue() {
        assertNotNull(IdGenerateStrategy.LEAF_SEGMENT);
        assertEquals("LEAF_SEGMENT", IdGenerateStrategy.LEAF_SEGMENT.name());
    }

    @Test
    void shouldFindEnumByName() {
        assertEquals(IdGenerateStrategy.ID_TEMP, IdGenerateStrategy.valueOf("ID_TEMP"));
        assertEquals(IdGenerateStrategy.LEAF_SEGMENT, IdGenerateStrategy.valueOf("LEAF_SEGMENT"));
    }
}
