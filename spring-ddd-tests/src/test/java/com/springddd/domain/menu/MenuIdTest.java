package com.springddd.domain.menu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuIdTest {

    @Test
    void shouldCreateMenuIdWithValue() {
        Long expectedValue = 1L;
        MenuId menuId = new MenuId(expectedValue);
        assertEquals(expectedValue, menuId.value());
    }

    @Test
    void shouldReturnValueFromLongWrapper() {
        Long value = 123L;
        MenuId menuId = new MenuId(value);
        assertEquals(123L, menuId.value());
    }

    @Test
    void shouldHandleNullValue() {
        MenuId menuId = new MenuId(null);
        assertNull(menuId.value());
    }
}
