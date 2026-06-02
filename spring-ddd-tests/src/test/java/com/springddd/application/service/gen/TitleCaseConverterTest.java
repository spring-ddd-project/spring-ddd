package com.springddd.application.service.gen;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TitleCaseConverterTest {
    @Test
    void toTitleCase_shouldReturnNull_whenInputIsNull() {
        assertNull(TitleCaseConverter.toTitleCase(null));
    }
    @Test
    void toTitleCase_shouldReturnEmpty_whenInputIsEmpty() {
        assertEquals("", TitleCaseConverter.toTitleCase(""));
    }
    @Test
    void toTitleCase_shouldHandleUnderscore() {
        assertEquals("Hello World", TitleCaseConverter.toTitleCase("hello_world"));
    }
    @Test
    void toTitleCase_shouldHandleMultipleUnderscores() {
        assertEquals("Hello World Foo", TitleCaseConverter.toTitleCase("hello_world_foo"));
    }
    @Test
    void toTitleCase_shouldHandleEmptyParts() {
        assertEquals("Hello World", TitleCaseConverter.toTitleCase("hello__world"));
    }
}
