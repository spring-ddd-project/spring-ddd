package com.springddd.application.service.gen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TitleCaseConverterTest {

    @Test
    void toTitleCase_shouldConvertSnakeCaseToTitleCase() {
        assertEquals("Hello World", TitleCaseConverter.toTitleCase("hello_world"));
        assertEquals("Test String", TitleCaseConverter.toTitleCase("test_string"));
    }

    @Test
    void toTitleCase_shouldHandleMultipleUnderscores() {
        assertEquals("Hello World Test", TitleCaseConverter.toTitleCase("hello_world_test"));
    }

    @Test
    void toTitleCase_shouldHandleEmptyString() {
        assertEquals("", TitleCaseConverter.toTitleCase(""));
    }

    @Test
    void toTitleCase_shouldHandleNull() {
        assertNull(TitleCaseConverter.toTitleCase(null));
    }

    @Test
    void toTitleCase_shouldHandleStringWithoutUnderscores() {
        assertEquals("Hello", TitleCaseConverter.toTitleCase("hello"));
    }

    @Test
    void toTitleCase_shouldCapitalizeFirstLetter() {
        assertEquals("Hello World", TitleCaseConverter.toTitleCase("hello_world"));
        assertEquals("Api Response", TitleCaseConverter.toTitleCase("api_response"));
    }

    @Test
    void toTitleCase_shouldHandleConsecutiveUnderscores() {
        assertEquals("Hello World", TitleCaseConverter.toTitleCase("hello__world"));
    }

    @Test
    void toTitleCase_shouldHandleUnderscoreAtStart() {
        // Leading underscore produces empty first part, so result starts with the word after underscore
        assertEquals("Hello", TitleCaseConverter.toTitleCase("_hello"));
    }
}
