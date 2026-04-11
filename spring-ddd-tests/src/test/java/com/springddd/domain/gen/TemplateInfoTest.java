package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.TemplateContentNullException;
import com.springddd.domain.gen.exception.TemplateNameNullException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TemplateInfoTest {

    @Test
    void shouldCreateTemplateInfoWithValidValues() {
        TemplateInfo info = new TemplateInfo("template1", "template content here");
        assertEquals("template1", info.templateName());
        assertEquals("template content here", info.templateContent());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        TemplateInfo info1 = new TemplateInfo("template1", "template content here");
        TemplateInfo info2 = new TemplateInfo("template1", "template content here");
        assertEquals(info1, info2);
    }

    @Test
    void toString_shouldReturnValueAsString() {
        TemplateInfo info = new TemplateInfo("template1", "template content here");
        String str = info.toString();
        assertTrue(str.contains("TemplateInfo"));
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThrows(TemplateNameNullException.class, () ->
            new TemplateInfo(null, "template content"));
    }

    @Test
    void shouldThrowWhenContentIsNull() {
        assertThrows(TemplateContentNullException.class, () ->
            new TemplateInfo("template1", null));
    }

    @Test
    void shouldThrowWhenNameIsEmpty() {
        assertThrows(TemplateNameNullException.class, () ->
            new TemplateInfo("", "template content"));
    }

    @Test
    void shouldThrowWhenContentIsEmpty() {
        assertThrows(TemplateContentNullException.class, () ->
            new TemplateInfo("template1", ""));
    }
}
