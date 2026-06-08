package com.springddd.domain.menu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuExtendInfoTest {

    @Test
    void constructor_with6Params_shouldSetAllFields() {
        Integer order = 1;
        String title = "Menu Title";
        Integer menuType = 1;
        String icon = "icon";
        Boolean menuStatus = true;
        Boolean visible = true;

        MenuExtendInfo info = new MenuExtendInfo(order, title, menuType, icon, menuStatus, visible);

        assertEquals(order, info.order());
        assertEquals(title, info.title());
        assertEquals(menuType, info.menuType());
        assertEquals(icon, info.icon());
        assertEquals(menuStatus, info.menuStatus());
        assertEquals(visible, info.visible());
    }

    @Test
    void constructor_with3Params_shouldSetOnlyRequiredFields() {
        Integer order = 1;
        Integer menuType = 3;
        Boolean menuStatus = false;

        MenuExtendInfo info = new MenuExtendInfo(order, menuType, menuStatus);

        assertEquals(order, info.order());
        assertEquals(menuType, info.menuType());
        assertEquals(menuStatus, info.menuStatus());
        assertNull(info.title());
        assertNull(info.icon());
        assertNull(info.visible());
    }

    @Test
    void constructor_with5Params_shouldSetMenuTypeFields() {
        Integer order = 2;
        String title = "Title";
        String icon = "icon-path";
        Integer menuType = 2;
        Boolean visible = false;

        MenuExtendInfo info = new MenuExtendInfo(order, title, icon, menuType, visible, true);

        assertEquals(order, info.order());
        assertEquals(title, info.title());
        assertEquals(icon, info.icon());
        assertEquals(menuType, info.menuType());
        assertEquals(visible, info.visible());
    }

    @Test
    void order_shouldBeAccessible() {
        MenuExtendInfo info = new MenuExtendInfo(5, "Title", 1, "icon", true, true);
        assertEquals(5, info.order());
    }

    @Test
    void title_shouldBeAccessible() {
        MenuExtendInfo info = new MenuExtendInfo(1, "Menu Title", 1, "icon", true, true);
        assertEquals("Menu Title", info.title());
    }

    @Test
    void menuType_shouldBeAccessible() {
        MenuExtendInfo info = new MenuExtendInfo(1, "Title", 2, "icon", true, true);
        assertEquals(2, info.menuType());
    }

    @Test
    void icon_shouldBeAccessible() {
        MenuExtendInfo info = new MenuExtendInfo(1, "Title", 1, "el-icon-menu", true, true);
        assertEquals("el-icon-menu", info.icon());
    }

    @Test
    void menuStatus_shouldBeAccessible() {
        MenuExtendInfo info = new MenuExtendInfo(1, "Title", 1, "icon", false, true);
        assertFalse(info.menuStatus());
    }

    @Test
    void visible_shouldBeAccessible() {
        MenuExtendInfo info = new MenuExtendInfo(1, "Title", 1, "icon", true, false);
        assertFalse(info.visible());
    }
}
