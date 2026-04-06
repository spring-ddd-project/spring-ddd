package com.springddd.application.service.menu;

import com.springddd.domain.menu.Button;
import com.springddd.domain.menu.Catalog;
import com.springddd.domain.menu.Menu;
import com.springddd.domain.menu.MenuExtendInfo;
import com.springddd.domain.menu.SysMenuDomain;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysMenuDomainButtonStrategyTest {

    @Test
    void check_shouldReturnTrue_whenTypeIs3() {
        SysMenuDomainButtonStrategy strategy = new SysMenuDomainButtonStrategy();
        assertTrue(strategy.check(3));
    }

    @Test
    void check_shouldReturnFalse_whenTypeIsNot3() {
        SysMenuDomainButtonStrategy strategy = new SysMenuDomainButtonStrategy();
        assertFalse(strategy.check(1));
        assertFalse(strategy.check(2));
        assertFalse(strategy.check(0));
    }

    @Test
    void handle_shouldCreateSysMenuDomain_withButton() {
        SysMenuDomainButtonStrategy strategy = new SysMenuDomainButtonStrategy();

        Catalog catalog = new Catalog("redirect/path");
        Menu menu = new Menu("/path", "component", true, false, false);
        Button button = new Button("permission:read", "/api/read");
        MenuExtendInfo extendInfo = new MenuExtendInfo(1, 1, true);

        SysMenuDomain domain = strategy.handle("TestMenu", catalog, menu, button, extendInfo);

        assertNotNull(domain);
        assertEquals("TestMenu", domain.getName());
        assertNotNull(domain.getButton());
        assertEquals("permission:read", domain.getButton().permission());
        assertEquals("/api/read", domain.getButton().api());
        assertNotNull(domain.getMenuExtendInfo());
    }

    @Test
    void handle_shouldSetCorrectExtendInfo() {
        SysMenuDomainButtonStrategy strategy = new SysMenuDomainButtonStrategy();

        Catalog catalog = new Catalog("redirect");
        Menu menu = new Menu("/path2", "component2", false, true, false);
        Button button = new Button("permission:write", "/api/write");
        MenuExtendInfo extendInfo = new MenuExtendInfo(2, 1, false);

        SysMenuDomain domain = strategy.handle("Name", catalog, menu, button, extendInfo);

        assertEquals(2, domain.getMenuExtendInfo().order());
        assertEquals(1, domain.getMenuExtendInfo().menuType());
        assertFalse(domain.getMenuExtendInfo().menuStatus());
    }
}