package com.springddd.domain;

import com.springddd.domain.gen.AggregateId;
import com.springddd.domain.gen.ColumnsId;
import com.springddd.domain.gen.ColumnBindId;
import com.springddd.domain.gen.InfoId;
import com.springddd.domain.gen.TemplateId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AggregateRootIdTest {

    @Test
    void shouldImplementAggregateRootIdInterface() {
        AggregateId aggregateId = new AggregateId(1L);
        assertTrue(aggregateId instanceof AggregateRootId);
        assertEquals(1L, aggregateId.value());
    }

    @Test
    void shouldImplementAggregateRootIdWithColumnsId() {
        ColumnsId columnsId = new ColumnsId(100L);
        assertTrue(columnsId instanceof AggregateRootId);
        assertEquals(100L, columnsId.value());
    }

    @Test
    void shouldImplementAggregateRootIdWithColumnBindId() {
        ColumnBindId columnBindId = new ColumnBindId(200L);
        assertTrue(columnBindId instanceof AggregateRootId);
        assertEquals(200L, columnBindId.value());
    }

    @Test
    void shouldImplementAggregateRootIdWithInfoId() {
        InfoId infoId = new InfoId(300L);
        assertTrue(infoId instanceof AggregateRootId);
        assertEquals(300L, infoId.value());
    }

    @Test
    void shouldImplementAggregateRootIdWithTemplateId() {
        TemplateId templateId = new TemplateId(400L);
        assertTrue(templateId instanceof AggregateRootId);
        assertEquals(400L, templateId.value());
    }

    @Test
    void shouldReturnCorrectValue() {
        AggregateId id1 = new AggregateId(1L);
        AggregateId id2 = new AggregateId(1L);
        AggregateId id3 = new AggregateId(2L);

        assertEquals(id1.value(), id2.value());
        assertNotEquals(id1.value(), id3.value());
    }

    @Test
    void shouldHandleNullValue() {
        AggregateId id = new AggregateId(null);
        assertTrue(id instanceof AggregateRootId);
        assertNull(id.value());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        AggregateId id1 = new AggregateId(1L);
        AggregateId id2 = new AggregateId(1L);
        AggregateId id3 = new AggregateId(2L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, id3);
    }

    @Test
    void shouldImplementToString() {
        AggregateId id = new AggregateId(42L);
        String str = id.toString();
        assertNotNull(str);
        assertTrue(str.contains("42"));
    }
}
