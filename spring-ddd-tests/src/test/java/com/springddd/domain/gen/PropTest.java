package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PropTest {

    @Test
    void shouldCreatePropWithValidValues() {
        Prop prop = new Prop("PRI", "id", "bigint", "Primary key", "Long", "Long");
        assertEquals("PRI", prop.propColumnKey());
        assertEquals("id", prop.propColumnName());
        assertEquals("bigint", prop.propColumnType());
        assertEquals("Primary key", prop.propColumnComment());
        assertEquals("Long", prop.propJavaType());
        assertEquals("Long", prop.propJavaEntity());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        Prop prop1 = new Prop("PRI", "id", "bigint", "Primary key", "Long", "Long");
        Prop prop2 = new Prop("PRI", "id", "bigint", "Primary key", "Long", "Long");
        assertEquals(prop1, prop2);
    }

    @Test
    void toString_shouldReturnValueAsString() {
        Prop prop = new Prop("PRI", "id", "bigint", "Primary key", "Long", "Long");
        String str = prop.toString();
        assertTrue(str.contains("Prop"));
    }

    @Test
    void shouldThrowWhenColumnNameIsNull() {
        assertThrows(ColumnNameNullException.class, () ->
            new Prop("PRI", null, "bigint", "Primary key", "Long", "Long"));
    }

    @Test
    void shouldThrowWhenColumnTypeIsNull() {
        assertThrows(ColumnTypeNullException.class, () ->
            new Prop("PRI", "id", null, "Primary key", "Long", "Long"));
    }

    @Test
    void shouldThrowWhenColumnCommentIsNull() {
        assertThrows(ColumnCommentNullException.class, () ->
            new Prop("PRI", "id", "bigint", null, "Long", "Long"));
    }

    @Test
    void shouldThrowWhenJavaTypeIsNull() {
        assertThrows(JavaTypeNullException.class, () ->
            new Prop("PRI", "id", "bigint", "Primary key", null, "Long"));
    }

    @Test
    void shouldThrowWhenJavaEntityIsNull() {
        assertThrows(JavaEntityNullException.class, () ->
            new Prop("PRI", "id", "bigint", "Primary key", "Long", null));
    }
}
