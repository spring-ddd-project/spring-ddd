package com.springddd.domain.menu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuExtendInfoTest {

    @Test
    void shouldCreateMenuExtendInfoWithAllFields() {
        MenuExtendInfo info = new MenuExtendInfo(1, "title", "icon", 1, true, true);
        assertEquals(1, info.order());
        assertEquals("title", info.title());
        assertEquals("icon", info.icon());
        assertEquals(1, info.menuType());
        assertTrue(info.visible());
        assertTrue(info.menuStatus());
    }

    @Test
    void shouldCreateMenuExtendInfoForCatalog() {
        MenuExtendInfo info = new MenuExtendInfo(1, "title", 1, "icon", true, true);
        assertEquals(1, info.order());
        assertEquals("title", info.title());
        assertEquals("icon", info.icon());
        assertEquals(1, info.menuType());
        assertTrue(info.visible());
        assertTrue(info.menuStatus());
    }

    @Test
    void shouldCreateMenuExtendInfoForButton() {
        MenuExtendInfo info = new MenuExtendInfo(1, 1, true);
        assertEquals(1, info.order());
        assertNull(info.title());
        assertNull(info.icon());
        assertEquals(1, info.menuType());
        assertNull(info.visible());
        assertTrue(info.menuStatus());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        MenuExtendInfo info1 = new MenuExtendInfo(1, "title", "icon", 1, true, true);
        MenuExtendInfo info2 = new MenuExtendInfo(1, "title", "icon", 1, true, true);
        assertEquals(info1, info2);
    }

    @Test
    void toString_shouldReturnValueAsString() {
        MenuExtendInfo info = new MenuExtendInfo(1, "title", "icon", 1, true, true);
        String str = info.toString();
        assertTrue(str.contains("MenuExtendInfo"));
    }
}
