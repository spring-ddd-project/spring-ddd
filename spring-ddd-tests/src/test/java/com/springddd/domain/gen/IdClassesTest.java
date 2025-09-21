package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdClassesTest {

    // AggregateId tests
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
    void aggregateId_equals_forDifferentValue_shouldNotBeEqual() {
        AggregateId id1 = new AggregateId(1L);
        AggregateId id2 = new AggregateId(2L);
        assertNotEquals(id1, id2);
    }

    @Test
    void aggregateId_hashCode_forSameValue_shouldBeEqual() {
        AggregateId id1 = new AggregateId(1L);
        AggregateId id2 = new AggregateId(1L);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void aggregateId_toString_shouldReturnCorrectString() {
        AggregateId id = new AggregateId(1L);
        assertEquals("AggregateId[value=1]", id.toString());
    }

    // ColumnsId tests
    @Test
    void columnsId_withValidValue_shouldCreateSuccessfully() {
        ColumnsId id = new ColumnsId(1L);
        assertEquals(1L, id.value());
    }

    @Test
    void columnsId_equals_forSameValue_shouldBeEqual() {
        ColumnsId id1 = new ColumnsId(1L);
        ColumnsId id2 = new ColumnsId(1L);
        assertEquals(id1, id2);
    }

    @Test
    void columnsId_toString_shouldReturnCorrectString() {
        ColumnsId id = new ColumnsId(1L);
        assertEquals("ColumnsId[value=1]", id.toString());
    }

    // InfoId tests
    @Test
    void infoId_withValidValue_shouldCreateSuccessfully() {
        InfoId id = new InfoId(1L);
        assertEquals(1L, id.value());
    }

    @Test
    void infoId_equals_forSameValue_shouldBeEqual() {
        InfoId id1 = new InfoId(1L);
        InfoId id2 = new InfoId(1L);
        assertEquals(id1, id2);
    }

    @Test
    void infoId_toString_shouldReturnCorrectString() {
        InfoId id = new InfoId(1L);
        assertEquals("InfoId[value=1]", id.toString());
    }

    // ColumnBindId tests
    @Test
    void columnBindId_withValidValue_shouldCreateSuccessfully() {
        ColumnBindId id = new ColumnBindId(1L);
        assertEquals(1L, id.value());
    }

    @Test
    void columnBindId_equals_forSameValue_shouldBeEqual() {
        ColumnBindId id1 = new ColumnBindId(1L);
        ColumnBindId id2 = new ColumnBindId(1L);
        assertEquals(id1, id2);
    }

    @Test
    void columnBindId_toString_shouldReturnCorrectString() {
        ColumnBindId id = new ColumnBindId(1L);
        assertEquals("ColumnBindId[value=1]", id.toString());
    }

    // TemplateId tests
    @Test
    void templateId_withValidValue_shouldCreateSuccessfully() {
        TemplateId id = new TemplateId(1L);
        assertEquals(1L, id.value());
    }

    @Test
    void templateId_equals_forSameValue_shouldBeEqual() {
        TemplateId id1 = new TemplateId(1L);
        TemplateId id2 = new TemplateId(1L);
        assertEquals(id1, id2);
    }

    @Test
    void templateId_toString_shouldReturnCorrectString() {
        TemplateId id = new TemplateId(1L);
        assertEquals("TemplateId[value=1]", id.toString());
    }

    // Edge cases
    @Test
    void allIdClasses_withZeroValue_shouldCreateSuccessfully() {
        assertEquals(0L, new AggregateId(0L).value());
        assertEquals(0L, new ColumnsId(0L).value());
        assertEquals(0L, new InfoId(0L).value());
        assertEquals(0L, new ColumnBindId(0L).value());
        assertEquals(0L, new TemplateId(0L).value());
    }

    @Test
    void allIdClasses_withNegativeValue_shouldCreateSuccessfully() {
        assertEquals(-1L, new AggregateId(-1L).value());
        assertEquals(-1L, new ColumnsId(-1L).value());
        assertEquals(-1L, new InfoId(-1L).value());
        assertEquals(-1L, new ColumnBindId(-1L).value());
        assertEquals(-1L, new TemplateId(-1L).value());
    }
}
