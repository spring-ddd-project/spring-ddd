package com.springddd.domain.menu;

import com.springddd.domain.menu.exception.MenuComponentNullException;
import com.springddd.domain.menu.exception.MenuPathNullException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    @Test
    void shouldCreateMenuWithValidValues() {
        Menu menu = new Menu("/path", "/component.vue", false, false, false);
        assertEquals("/path", menu.menuPath());
        assertEquals("/component.vue", menu.component());
        assertFalse(menu.affixTab());
        assertFalse(menu.noBasicLayout());
        assertFalse(menu.embedded());
    }

    @Test
    void shouldCreateMenuWithTrueFlags() {
        Menu menu = new Menu("/path", "/component.vue", true, true, true);
        assertEquals("/path", menu.menuPath());
        assertEquals("/component.vue", menu.component());
        assertTrue(menu.affixTab());
        assertTrue(menu.noBasicLayout());
        assertTrue(menu.embedded());
    }

    @Test
    void shouldThrowMenuPathNullExceptionWhenPathIsNull() {
        assertThrows(MenuPathNullException.class, () -> {
            new Menu(null, "/component.vue", false, false, false);
        });
    }

    @Test
    void shouldThrowMenuPathNullExceptionWhenPathIsEmpty() {
        assertThrows(MenuPathNullException.class, () -> {
            new Menu("", "/component.vue", false, false, false);
        });
    }

    @Test
    void shouldThrowMenuComponentNullExceptionWhenComponentIsNull() {
        assertThrows(MenuComponentNullException.class, () -> {
            new Menu("/path", null, false, false, false);
        });
    }

    @Test
    void shouldThrowMenuComponentNullExceptionWhenComponentIsEmpty() {
        assertThrows(MenuComponentNullException.class, () -> {
            new Menu("/path", "", false, false, false);
        });
    }

    @Test
    void equals_shouldWorkForSameValues() {
        Menu menu1 = new Menu("/path", "/component.vue", false, false, false);
        Menu menu2 = new Menu("/path", "/component.vue", false, false, false);
        assertEquals(menu1, menu2);
    }

    @Test
    void equals_shouldFailForDifferentPath() {
        Menu menu1 = new Menu("/path1", "/component.vue", false, false, false);
        Menu menu2 = new Menu("/path2", "/component.vue", false, false, false);
        assertNotEquals(menu1, menu2);
    }

    @Test
    void equals_shouldFailForDifferentComponent() {
        Menu menu1 = new Menu("/path", "/component1.vue", false, false, false);
        Menu menu2 = new Menu("/path", "/component2.vue", false, false, false);
        assertNotEquals(menu1, menu2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        Menu menu1 = new Menu("/path", "/component.vue", false, false, false);
        Menu menu2 = new Menu("/path", "/component.vue", false, false, false);
        assertEquals(menu1.hashCode(), menu2.hashCode());
    }

    @Test
    void toString_shouldReturnValues() {
        Menu menu = new Menu("/path", "/component.vue", false, false, false);
        String str = menu.toString();
        assertTrue(str.contains("/path"));
        assertTrue(str.contains("/component.vue"));
    }
}
