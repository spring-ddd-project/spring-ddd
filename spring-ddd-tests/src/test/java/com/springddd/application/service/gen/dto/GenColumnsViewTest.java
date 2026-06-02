package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenColumnsViewTest {

    @Test
    void shouldCreateWithNoArgsConstructor() {
        GenColumnsView view = new GenColumnsView();
        assertNull(view.getId());
        assertNull(view.getPropColumnName());
    }

    @Test
    void shouldCreateWithAllArgsConstructor() {
        GenColumnsView view = new GenColumnsView(
                1L, 2L, "PRI", "id", "bigint", "Primary Key",
                "id", "Long", 3L, "dict", true, true, true,
                (byte) 1, (byte) 2, (byte) 3, (byte) 4,
                "filterComp", "filterType", "tsType", "formComp",
                true, true, "en", "locale"
        );
        assertEquals(1L, view.getId());
        assertEquals("id", view.getPropColumnName());
    }

    @Test
    void shouldCreateWithCustomConstructor() {
        GenColumnsView view = new GenColumnsView("PRI", "id", "bigint", "Primary Key");
        assertEquals("PRI", view.getPropColumnKey());
        assertEquals("id", view.getPropColumnName());
        assertEquals("bigint", view.getPropColumnType());
        assertEquals("Primary Key", view.getPropColumnComment());
    }

    @Test
    void shouldSetAndGetFields() {
        GenColumnsView view = new GenColumnsView();
        view.setId(1L);
        view.setPropJavaType("String");
        view.setFormComponentStr("Input");
        assertEquals(1L, view.getId());
        assertEquals("String", view.getPropJavaType());
        assertEquals("Input", view.getFormComponentStr());
    }
}
