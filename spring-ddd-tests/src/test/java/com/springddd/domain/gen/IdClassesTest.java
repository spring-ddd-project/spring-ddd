package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdClassesTest {

    @Test
    void aggregateId_withValidValue_shouldCreateSuccessfully() {
        AggregateId id = new AggregateId(1L);
        assertEquals(1L, id.value());
    }

    @Test
    void aggregateId_equals_forSameValue_shouldBeEqual() {
        AggregateId id1 = new AggregateId(1L);
        AggregateId id2 = new AggregateId(1L);
        assertEquals(id1, id2);
    }

    @Test
    void columnsId_withValidValue_shouldCreateSuccessfully() {
        ColumnsId id = new ColumnsId(1L);
        assertEquals(1L, id.value());
    }

    @Test
    void infoId_withValidValue_shouldCreateSuccessfully() {
        InfoId id = new InfoId(1L);
        assertEquals(1L, id.value());
    }

    @Test
    void columnBindId_withValidValue_shouldCreateSuccessfully() {
        ColumnBindId id = new ColumnBindId(1L);
        assertEquals(1L, id.value());
    }

    @Test
    void templateId_withValidValue_shouldCreateSuccessfully() {
        TemplateId id = new TemplateId(1L);
        assertEquals(1L, id.value());
    }
}
