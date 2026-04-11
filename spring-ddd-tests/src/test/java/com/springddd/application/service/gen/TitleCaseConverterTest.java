package com.springddd.application.service.gen;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TitleCaseConverterTest {

    @Test
    void shouldConvertToTitleCase() {
        assertEquals("Hello World", TitleCaseConverter.toTitleCase("hello_world"));
    }

    @Test
    void shouldReturnNullAsIs() {
        assertNull(TitleCaseConverter.toTitleCase(null));
    }

    @Test
    void shouldReturnEmptyAsIs() {
        assertEquals("", TitleCaseConverter.toTitleCase(""));
    }

    @Test
    void shouldHandleSingleWord() {
        assertEquals("Hello", TitleCaseConverter.toTitleCase("hello"));
    }

    @Test
    void shouldHandleMultipleUnderscores() {
        assertEquals("Hello World Foo Bar", TitleCaseConverter.toTitleCase("hello_world_foo_bar"));
    }

    @Test
    void shouldSkipEmptyParts() {
        assertEquals("Hello World", TitleCaseConverter.toTitleCase("hello__world"));
    }
}
