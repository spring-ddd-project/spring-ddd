package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GenAggregateValueObjectTest {

    @Test
    void shouldCreateGenAggregateValueObject() {
        GenAggregateValueObject obj = new GenAggregateValueObject("name", "value", (byte) 1);
        assertEquals("name", obj.objectName());
        assertEquals("value", obj.objectValue());
        assertEquals((byte) 1, obj.objectType());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        GenAggregateValueObject obj1 = new GenAggregateValueObject("name", "value", (byte) 1);
        GenAggregateValueObject obj2 = new GenAggregateValueObject("name", "value", (byte) 1);
        assertEquals(obj1, obj2);
    }

    @Test
    void toString_shouldReturnValueAsString() {
        GenAggregateValueObject obj = new GenAggregateValueObject("name", "value", (byte) 1);
        String str = obj.toString();
        assertTrue(str.contains("GenAggregateValueObject"));
    }
}
