package com.springddd.application.service.gen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TitleCaseConverterTest {

    @Test
    void toTitleCase_shouldConvertToTitleCase() {
        assertEquals("Hello World", TitleCaseConverter.toTitleCase("hello_world"));
        assertEquals("Test String", TitleCaseConverter.toTitleCase("test_string"));
    }

    @Test
    void toTitleCase_shouldHandleEmptyString() {
        assertEquals("", TitleCaseConverter.toTitleCase(""));
    }

    @Test
    void toTitleCase_shouldHandleNull() {
        assertNull(TitleCaseConverter.toTitleCase(null));
    }
}
