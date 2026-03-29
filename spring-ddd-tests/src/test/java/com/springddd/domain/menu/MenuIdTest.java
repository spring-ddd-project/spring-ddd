package com.springddd.domain.menu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuIdTest {

    @Test
    void shouldCreateMenuIdWithValidValue() {
        MenuId menuId = new MenuId(1L);
        assertEquals(1L, menuId.value());
    }

    @Test
    void shouldCreateMenuIdWithZero() {
        MenuId menuId = new MenuId(0L);
        assertEquals(0L, menuId.value());
    }

    @Test
    void shouldCreateMenuIdWithLargeValue() {
        MenuId menuId = new MenuId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, menuId.value());
    }

    @Test
    void equals_shouldWorkForSameValue() {
        MenuId id1 = new MenuId(1L);
        MenuId id2 = new MenuId(1L);
        assertEquals(id1, id2);
    }

    @Test
    void equals_shouldFailForDifferentValue() {
        MenuId id1 = new MenuId(1L);
        MenuId id2 = new MenuId(2L);
        assertNotEquals(id1, id2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        MenuId id1 = new MenuId(1L);
        MenuId id2 = new MenuId(1L);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        MenuId menuId = new MenuId(123L);
        assertEquals("123", menuId.toString());
    }
}
