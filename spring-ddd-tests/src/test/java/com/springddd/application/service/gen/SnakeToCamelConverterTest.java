package com.springddd.application.service.gen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SnakeToCamelConverterTest {

    @Test
    void convertToCamelCase_shouldConvertSnakeCaseToCamelCase() {
        assertEquals("helloWorld", SnakeToCamelConverter.convertToCamelCase("hello_world"));
        assertEquals("testString", SnakeToCamelConverter.convertToCamelCase("test_string"));
    }

    @Test
    void convertToCamelCase_shouldHandleEmptyString() {
        assertEquals("", SnakeToCamelConverter.convertToCamelCase(""));
    }

    @Test
    void convertToCamelCase_shouldHandleNull() {
        assertNull(SnakeToCamelConverter.convertToCamelCase(null));
    }
}
