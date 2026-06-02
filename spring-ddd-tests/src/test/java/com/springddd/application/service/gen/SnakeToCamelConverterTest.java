package com.springddd.application.service.gen;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SnakeToCamelConverterTest {
    @Test
    void convertToCamelCase_shouldReturnNull_whenInputIsNull() {
        assertNull(SnakeToCamelConverter.convertToCamelCase(null));
    }
    @Test
    void convertToCamelCase_shouldReturnEmpty_whenInputIsEmpty() {
        assertEquals("", SnakeToCamelConverter.convertToCamelCase(""));
    }
    @Test
    void convertToCamelCase_shouldHandleUnderscore() {
        assertEquals("helloWorld", SnakeToCamelConverter.convertToCamelCase("hello_world"));
    }
    @Test
    void convertToCamelCase_shouldHandleMultipleUnderscores() {
        assertEquals("helloWorldFoo", SnakeToCamelConverter.convertToCamelCase("hello_world_foo"));
    }
    @Test
    void convertToCamelCase_shouldHandleLeadingUnderscore() {
        assertEquals("HelloWorld", SnakeToCamelConverter.convertToCamelCase("_hello_world"));
    }
}
