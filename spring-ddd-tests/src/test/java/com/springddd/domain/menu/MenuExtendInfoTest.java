package com.springddd.domain.menu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuExtendInfoTest {

    @Test
    void shouldCreateMenuExtendInfoWithAllFields() {
        Integer order = 1;
        String title = "Dashboard";
        String icon = "dashboard";
        Integer menuType = 1;
        Boolean visible = true;
        Boolean menuStatus = true;

        MenuExtendInfo info = new MenuExtendInfo(order, title, icon, menuType, visible, menuStatus);

        assertEquals(order, info.order());
        assertEquals(title, info.title());
        assertEquals(icon, info.icon());
        assertEquals(menuType, info.menuType());
        assertEquals(visible, info.visible());
        assertEquals(menuStatus, info.menuStatus());
    }

    @Test
    void shouldCreateMenuExtendInfoViaCatalogConstructor() {
        Integer order = 2;
        String title = "System";
        Integer menuType = 0;
        String icon = "setting";
        Boolean menuStatus = true;
        Boolean visible = false;

        MenuExtendInfo info = new MenuExtendInfo(order, title, menuType, icon, menuStatus, visible);

        assertEquals(order, info.order());
        assertEquals(title, info.title());
        assertEquals(icon, info.icon());
        assertEquals(menuType, info.menuType());
        assertEquals(visible, info.visible());
        assertEquals(menuStatus, info.menuStatus());
    }

    @Test
    void shouldCreateMenuExtendInfoViaButtonConstructor() {
        Integer order = 3;
        Integer menuType = 2;
        Boolean menuStatus = true;

        MenuExtendInfo info = new MenuExtendInfo(order, menuType, menuStatus);

        assertEquals(order, info.order());
        assertNull(info.title());
        assertNull(info.icon());
        assertEquals(menuType, info.menuType());
        assertNull(info.visible());
        assertEquals(menuStatus, info.menuStatus());
    }

    @Test
    void shouldHandleNullFieldsInPrimaryConstructor() {
        MenuExtendInfo info = new MenuExtendInfo(null, null, null, (Integer) null, null, null);
        assertNull(info.order());
        assertNull(info.title());
        assertNull(info.icon());
        assertNull(info.menuType());
        assertNull(info.visible());
        assertNull(info.menuStatus());
    }

    @Test
    void shouldHandleZeroOrder() {
        MenuExtendInfo info = new MenuExtendInfo(0, "Home", "home", 1, true, true);
        assertEquals(0, info.order());
    }
}
