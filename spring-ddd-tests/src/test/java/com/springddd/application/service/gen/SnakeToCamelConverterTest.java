package com.springddd.application.service.gen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SnakeToCamelConverterTest {

    @Test
    void convertToCamelCase_shouldConvertSnakeCaseToCamelCase() {
        assertEquals("helloWorld", SnakeToCamelConverter.convertToCamelCase("hello_world"));
        assertEquals("testString", SnakeToCamelConverter.convertToCamelCase("test_string"));
        assertEquals("apiResponse", SnakeToCamelConverter.convertToCamelCase("api_response"));
    }

    @Test
    void convertToCamelCase_shouldHandleMultipleUnderscores() {
        assertEquals("aBC", SnakeToCamelConverter.convertToCamelCase("a_b_c"));
        assertEquals("helloWorldTest", SnakeToCamelConverter.convertToCamelCase("hello_world_test"));
    }

    @Test
    void convertToCamelCase_shouldHandleEmptyString() {
        assertEquals("", SnakeToCamelConverter.convertToCamelCase(""));
    }

    @Test
    void convertToCamelCase_shouldHandleNull() {
        assertNull(SnakeToCamelConverter.convertToCamelCase(null));
    }

    @Test
    void convertToCamelCase_shouldHandleStringWithoutUnderscores() {
        assertEquals("hello", SnakeToCamelConverter.convertToCamelCase("hello"));
        assertEquals("helloworld", SnakeToCamelConverter.convertToCamelCase("helloworld"));
    }

    @Test
    void convertToCamelCase_shouldHandleStringStartingWithUnderscore() {
        assertEquals("Hello", SnakeToCamelConverter.convertToCamelCase("_hello"));
        assertEquals("TestValue", SnakeToCamelConverter.convertToCamelCase("_test_value"));
    }

    @Test
    void convertToCamelCase_shouldHandleStringEndingWithUnderscore() {
        assertEquals("hello", SnakeToCamelConverter.convertToCamelCase("hello_"));
    }

    @Test
    void convertToCamelCase_shouldHandleConsecutiveUnderscores() {
        assertEquals("helloWorld", SnakeToCamelConverter.convertToCamelCase("hello__world"));
    }
}
