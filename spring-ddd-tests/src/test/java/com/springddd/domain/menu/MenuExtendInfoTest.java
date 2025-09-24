package com.springddd.domain.menu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuExtendInfoTest {

    @Test
    void shouldCreateWithFullParameters() {
        MenuExtendInfo info = new MenuExtendInfo(1, "标题", "icon", 1, true, true);
        assertEquals(1, info.order());
        assertEquals("标题", info.title());
        assertEquals("icon", info.icon());
        assertEquals(1, info.menuType());
        assertTrue(info.visible());
        assertTrue(info.menuStatus());
    }

    @Test
    void shouldCreateWithCatalogConstructor() {
        MenuExtendInfo info = new MenuExtendInfo(2, "目录标题", 1, "folder", true, false);
        assertEquals(2, info.order());
        assertEquals("目录标题", info.title());
        assertEquals("folder", info.icon());
        assertEquals(1, info.menuType());
        assertTrue(info.visible());
        assertFalse(info.menuStatus());
    }

    @Test
    void shouldCreateWithButtonConstructor() {
        MenuExtendInfo info = new MenuExtendInfo(3, 1, false);
        assertEquals(3, info.order());
        assertNull(info.title());
        assertNull(info.icon());
        assertEquals(1, info.menuType());
        assertNull(info.visible());
        assertFalse(info.menuStatus());
    }

    @Test
    void shouldCreateWithZeroValues() {
        MenuExtendInfo info = new MenuExtendInfo(0, null, null, 0, false, false);
        assertEquals(0, info.order());
        assertNull(info.title());
        assertNull(info.icon());
        assertEquals(0, info.menuType());
        assertFalse(info.visible());
        assertFalse(info.menuStatus());
    }

    @Test
    void shouldCreateWithMaxValues() {
        MenuExtendInfo info = new MenuExtendInfo(Integer.MAX_VALUE, "标题", "icon", Integer.MAX_VALUE, true, true);
        assertEquals(Integer.MAX_VALUE, info.order());
        assertEquals("标题", info.title());
        assertEquals("icon", info.icon());
        assertEquals(Integer.MAX_VALUE, info.menuType());
        assertTrue(info.visible());
        assertTrue(info.menuStatus());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        MenuExtendInfo info1 = new MenuExtendInfo(1, "标题", "icon", 1, true, true);
        MenuExtendInfo info2 = new MenuExtendInfo(1, "标题", "icon", 1, true, true);
        assertEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentOrder() {
        MenuExtendInfo info1 = new MenuExtendInfo(1, "标题", "icon", 1, true, true);
        MenuExtendInfo info2 = new MenuExtendInfo(2, "标题", "icon", 1, true, true);
        assertNotEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentTitle() {
        MenuExtendInfo info1 = new MenuExtendInfo(1, "标题1", "icon", 1, true, true);
        MenuExtendInfo info2 = new MenuExtendInfo(1, "标题2", "icon", 1, true, true);
        assertNotEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentMenuType() {
        MenuExtendInfo info1 = new MenuExtendInfo(1, "标题", "icon", 1, true, true);
        MenuExtendInfo info2 = new MenuExtendInfo(1, "标题", "icon", 2, true, true);
        assertNotEquals(info1, info2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        MenuExtendInfo info1 = new MenuExtendInfo(1, "标题", "icon", 1, true, true);
        MenuExtendInfo info2 = new MenuExtendInfo(1, "标题", "icon", 1, true, true);
        assertEquals(info1.hashCode(), info2.hashCode());
    }

    @Test
    void toString_shouldReturnValues() {
        MenuExtendInfo info = new MenuExtendInfo(1, "标题", "icon", 1, true, true);
        String str = info.toString();
        assertTrue(str.contains("1"));
        assertTrue(str.contains("标题"));
    }
}
