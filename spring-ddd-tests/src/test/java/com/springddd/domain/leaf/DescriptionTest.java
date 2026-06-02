package com.springddd.domain.leaf;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DescriptionTest {

    @Test
    void shouldCreateDescriptionWithValidValue() {
        Description description = new Description("test description");
        assertEquals("test description", description.value());
    }

    @Test
    void shouldConvertNullToEmptyString() {
        Description description = new Description(null);
        assertEquals("", description.value());
    }

    @Test
    void shouldAllowEmptyString() {
        Description description = new Description("");
        assertEquals("", description.value());
    }

    @Test
    void equals_shouldWorkForSameValue() {
        Description description1 = new Description("test");
        Description description2 = new Description("test");
        assertEquals(description1, description2);
    }

    @Test
    void equals_shouldFailForDifferentValue() {
        Description description1 = new Description("test1");
        Description description2 = new Description("test2");
        assertNotEquals(description1, description2);
    }

    @Test
    void hashCode_shouldBeConsistentForSameValue() {
        Description description1 = new Description("test");
        Description description2 = new Description("test");
        assertEquals(description1.hashCode(), description2.hashCode());
    }

    @Test
    void toString_shouldContainValue() {
        Description description = new Description("test");
        assertTrue(description.toString().contains("test"));
    }
}
