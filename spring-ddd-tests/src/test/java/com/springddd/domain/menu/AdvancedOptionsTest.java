package com.springddd.domain.menu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdvancedOptionsTest {

    @Test
    void shouldCreateAdvancedOptionsWithAllFields() {
        AdvancedOptions options = new AdvancedOptions(1, "icon", 1, true, true);
        assertEquals(1, options.order());
        assertEquals("icon", options.icon());
        assertEquals(1, options.menuType());
        assertTrue(options.visible());
        assertTrue(options.menuStatus());
    }

    @Test
    void shouldCreateAdvancedOptionsForCatalog() {
        AdvancedOptions options = new AdvancedOptions(1, 1, "icon", true, true);
        assertEquals(1, options.order());
        assertEquals("icon", options.icon());
        assertEquals(1, options.menuType());
        assertTrue(options.visible());
        assertTrue(options.menuStatus());
    }

    @Test
    void shouldCreateAdvancedOptionsForButton() {
        AdvancedOptions options = new AdvancedOptions(1, 1, true);
        assertEquals(1, options.order());
        assertNull(options.icon());
        assertEquals(1, options.menuType());
        assertNull(options.visible());
        assertTrue(options.menuStatus());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        AdvancedOptions opt1 = new AdvancedOptions(1, "icon", 1, true, true);
        AdvancedOptions opt2 = new AdvancedOptions(1, "icon", 1, true, true);
        assertEquals(opt1, opt2);
    }

    @Test
    void toString_shouldReturnValueAsString() {
        AdvancedOptions options = new AdvancedOptions(1, "icon", 1, true, true);
        String str = options.toString();
        assertTrue(str.contains("AdvancedOptions"));
    }
}
