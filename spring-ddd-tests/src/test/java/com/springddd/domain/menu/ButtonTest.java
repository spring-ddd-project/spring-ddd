package com.springddd.domain.menu;

import com.springddd.domain.menu.exception.MenuPermissionNullException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ButtonTest {

    @Test
    void shouldCreateButtonWithValidValues() {
        Button button = new Button("permission:edit", "/api/edit");
        assertEquals("permission:edit", button.permission());
        assertEquals("/api/edit", button.api());
    }

    @Test
    void shouldThrowMenuPermissionNullExceptionWhenPermissionIsNull() {
        assertThrows(MenuPermissionNullException.class, () -> {
            new Button(null, "/api/edit");
        });
    }

    @Test
    void shouldThrowMenuPermissionNullExceptionWhenPermissionIsEmpty() {
        assertThrows(MenuPermissionNullException.class, () -> {
            new Button("", "/api/edit");
        });
    }

    @Test
    void shouldAllowNullApi() {
        Button button = new Button("permission:view", null);
        assertEquals("permission:view", button.permission());
        assertNull(button.api());
    }

    @Test
    void shouldAllowEmptyApi() {
        Button button = new Button("permission:view", "");
        assertEquals("permission:view", button.permission());
        assertEquals("", button.api());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        Button button1 = new Button("permission:edit", "/api/edit");
        Button button2 = new Button("permission:edit", "/api/edit");
        assertEquals(button1, button2);
    }

    @Test
    void equals_shouldFailForDifferentPermission() {
        Button button1 = new Button("permission:edit", "/api/edit");
        Button button2 = new Button("permission:delete", "/api/edit");
        assertNotEquals(button1, button2);
    }

    @Test
    void equals_shouldFailForDifferentApi() {
        Button button1 = new Button("permission:edit", "/api/edit");
        Button button2 = new Button("permission:edit", "/api/delete");
        assertNotEquals(button1, button2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        Button button1 = new Button("permission:edit", "/api/edit");
        Button button2 = new Button("permission:edit", "/api/edit");
        assertEquals(button1.hashCode(), button2.hashCode());
    }

    @Test
    void toString_shouldReturnValues() {
        Button button = new Button("permission:edit", "/api/edit");
        String str = button.toString();
        assertTrue(str.contains("permission:edit"));
        assertTrue(str.contains("/api/edit"));
    }
}
