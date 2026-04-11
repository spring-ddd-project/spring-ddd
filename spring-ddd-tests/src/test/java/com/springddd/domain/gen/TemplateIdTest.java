package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemplateIdTest {

    @Test
    void shouldCreateTemplateIdWithValue() {
        TemplateId templateId = new TemplateId(1L);

        assertEquals(1L, templateId.value());
    }

    @Test
    void shouldCreateTemplateIdWithNullValue() {
        TemplateId templateId = new TemplateId(null);

        assertNull(templateId.value());
    }

    @Test
    void shouldCreateTemplateIdWithZeroValue() {
        TemplateId templateId = new TemplateId(0L);

        assertEquals(0L, templateId.value());
    }

    @Test
    void shouldCreateTemplateIdWithNegativeValue() {
        TemplateId templateId = new TemplateId(-30L);

        assertEquals(-30L, templateId.value());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        TemplateId id1 = new TemplateId(1L);
        TemplateId id2 = new TemplateId(1L);
        TemplateId id3 = new TemplateId(2L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, id3);
    }

    @Test
    void shouldImplementToString() {
        TemplateId templateId = new TemplateId(88L);

        assertEquals("TemplateId[value=88]", templateId.toString());
    }
}
