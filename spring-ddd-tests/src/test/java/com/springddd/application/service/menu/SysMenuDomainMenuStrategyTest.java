package com.springddd.application.service.menu;

import com.springddd.domain.menu.Button;
import com.springddd.domain.menu.Catalog;
import com.springddd.domain.menu.Menu;
import com.springddd.domain.menu.MenuExtendInfo;
import com.springddd.domain.menu.SysMenuDomain;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysMenuDomainMenuStrategyTest {

    @Test
    void check_shouldReturnTrue_whenTypeIs2() {
        SysMenuDomainMenuStrategy strategy = new SysMenuDomainMenuStrategy();
        assertTrue(strategy.check(2));
    }

    @Test
    void check_shouldReturnFalse_whenTypeIsNot2() {
        SysMenuDomainMenuStrategy strategy = new SysMenuDomainMenuStrategy();
        assertFalse(strategy.check(1));
        assertFalse(strategy.check(3));
        assertFalse(strategy.check(0));
    }

    @Test
    void handle_shouldCreateSysMenuDomain_withMenu() {
        SysMenuDomainMenuStrategy strategy = new SysMenuDomainMenuStrategy();

        Catalog catalog = new Catalog("redirect/path");
        Menu menu = new Menu("/path", "component", true, false, false);
        Button button = new Button("permission:read", "/api/read");
        MenuExtendInfo extendInfo = new MenuExtendInfo(1, "Title", 1, "icon", true, true);

        SysMenuDomain domain = strategy.handle("TestMenu", catalog, menu, button, extendInfo);

        assertNotNull(domain);
        assertEquals("TestMenu", domain.getName());
        assertNotNull(domain.getMenu());
        assertEquals("/path", domain.getMenu().menuPath());
        assertEquals("component", domain.getMenu().component());
        assertNotNull(domain.getMenuExtendInfo());
    }

    @Test
    void handle_shouldSetCorrectExtendInfo() {
        SysMenuDomainMenuStrategy strategy = new SysMenuDomainMenuStrategy();

        Catalog catalog = new Catalog("redirect");
        Menu menu = new Menu("/path2", "component2", false, true, false);
        Button button = new Button("permission:write", "/api/write");
        MenuExtendInfo extendInfo = new MenuExtendInfo(2, "Menu Title", 1, "setting", false, true);

        SysMenuDomain domain = strategy.handle("Name", catalog, menu, button, extendInfo);

        assertEquals(2, domain.getMenuExtendInfo().order());
        assertEquals("Menu Title", domain.getMenuExtendInfo().title());
        assertEquals(1, domain.getMenuExtendInfo().menuType());
        assertEquals("setting", domain.getMenuExtendInfo().icon());
        assertFalse(domain.getMenuExtendInfo().menuStatus());
        assertTrue(domain.getMenuExtendInfo().visible());
    }
}