package com.springddd.application.service.gen;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SnakeToCamelConverterTest {

    @Test
    void shouldConvertSnakeCaseToCamelCase() {
        assertEquals("helloWorld", SnakeToCamelConverter.convertToCamelCase("hello_world"));
    }

    @Test
    void shouldReturnNullAsIs() {
        assertNull(SnakeToCamelConverter.convertToCamelCase(null));
    }

    @Test
    void shouldReturnEmptyAsIs() {
        assertEquals("", SnakeToCamelConverter.convertToCamelCase(""));
    }

    @Test
    void shouldHandleSingleWord() {
        assertEquals("hello", SnakeToCamelConverter.convertToCamelCase("hello"));
    }

    @Test
    void shouldHandleMultipleUnderscores() {
        assertEquals("helloWorldFooBar", SnakeToCamelConverter.convertToCamelCase("hello_world_foo_bar"));
    }

    @Test
    void shouldHandleLeadingUnderscore() {
        assertEquals("Hello", SnakeToCamelConverter.convertToCamelCase("_hello"));
    }

    @Test
    void shouldHandleTrailingUnderscore() {
        assertEquals("hello", SnakeToCamelConverter.convertToCamelCase("hello_"));
    }
}
