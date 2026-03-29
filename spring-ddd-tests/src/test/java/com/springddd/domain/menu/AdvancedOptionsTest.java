package com.springddd.domain.menu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdvancedOptionsTest {

    @Test
    void shouldCreateWithFullParameters() {
        AdvancedOptions options = new AdvancedOptions(1, "icon", 1, true, true);
        assertEquals(1, options.order());
        assertEquals("icon", options.icon());
        assertEquals(1, options.menuType());
        assertTrue(options.visible());
        assertTrue(options.menuStatus());
    }

    @Test
    void shouldCreateWithCatalogConstructor() {
        AdvancedOptions options = new AdvancedOptions(2, 1, "folder", true, false);
        assertEquals(2, options.order());
        assertEquals("folder", options.icon());
        assertEquals(1, options.menuType());
        assertTrue(options.visible());
        assertFalse(options.menuStatus());
    }

    @Test
    void shouldCreateWithButtonConstructor() {
        AdvancedOptions options = new AdvancedOptions(3, 1, false);
        assertEquals(3, options.order());
        assertNull(options.icon());
        assertEquals(1, options.menuType());
        assertNull(options.visible());
        assertFalse(options.menuStatus());
    }

    @Test
    void shouldCreateWithZeroValues() {
        AdvancedOptions options = new AdvancedOptions(0, null, 0, false, false);
        assertEquals(0, options.order());
        assertNull(options.icon());
        assertEquals(0, options.menuType());
        assertFalse(options.visible());
        assertFalse(options.menuStatus());
    }

    @Test
    void shouldCreateWithMaxValues() {
        AdvancedOptions options = new AdvancedOptions(Integer.MAX_VALUE, "icon", Integer.MAX_VALUE, true, true);
        assertEquals(Integer.MAX_VALUE, options.order());
        assertEquals("icon", options.icon());
        assertEquals(Integer.MAX_VALUE, options.menuType());
        assertTrue(options.visible());
        assertTrue(options.menuStatus());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        AdvancedOptions options1 = new AdvancedOptions(1, "icon", 1, true, true);
        AdvancedOptions options2 = new AdvancedOptions(1, "icon", 1, true, true);
        assertEquals(options1, options2);
    }

    @Test
    void equals_shouldFailForDifferentOrder() {
        AdvancedOptions options1 = new AdvancedOptions(1, "icon", 1, true, true);
        AdvancedOptions options2 = new AdvancedOptions(2, "icon", 1, true, true);
        assertNotEquals(options1, options2);
    }

    @Test
    void equals_shouldFailForDifferentIcon() {
        AdvancedOptions options1 = new AdvancedOptions(1, "icon1", 1, true, true);
        AdvancedOptions options2 = new AdvancedOptions(1, "icon2", 1, true, true);
        assertNotEquals(options1, options2);
    }

    @Test
    void equals_shouldFailForDifferentMenuType() {
        AdvancedOptions options1 = new AdvancedOptions(1, "icon", 1, true, true);
        AdvancedOptions options2 = new AdvancedOptions(1, "icon", 2, true, true);
        assertNotEquals(options1, options2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        AdvancedOptions options1 = new AdvancedOptions(1, "icon", 1, true, true);
        AdvancedOptions options2 = new AdvancedOptions(1, "icon", 1, true, true);
        assertEquals(options1.hashCode(), options2.hashCode());
    }

    @Test
    void toString_shouldReturnValues() {
        AdvancedOptions options = new AdvancedOptions(1, "icon", 1, true, true);
        String str = options.toString();
        assertTrue(str.contains("1"));
        assertTrue(str.contains("icon"));
    }
}
