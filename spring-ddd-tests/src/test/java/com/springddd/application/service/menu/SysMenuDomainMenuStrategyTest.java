package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysMenuDomainMenuStrategyTest {

    private SysMenuDomainMenuStrategy menuStrategy;

    @BeforeEach
    void setUp() {
        menuStrategy = new SysMenuDomainMenuStrategy();
    }

    @Test
    void check_shouldReturnTrue_whenMenuTypeIsMenu() {
        assertTrue(menuStrategy.check(2));
    }

    @Test
    void check_shouldReturnFalse_whenMenuTypeIsNotMenu() {
        assertFalse(menuStrategy.check(1));
        assertFalse(menuStrategy.check(3));
        assertFalse(menuStrategy.check(0));
    }

    @Test
    void handle_shouldCreateDomainWithMenuInfo() {
        String name = "Test Menu";
        Catalog catalog = new Catalog("/redirect");
        Menu menu = new Menu("/path", "component.vue", true, false, true);
        Button button = new Button("permission", "/api");
        MenuExtendInfo menuExtendInfo = new MenuExtendInfo(1, "Title", "icon", 2, true, true);

        SysMenuDomain domain = menuStrategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertEquals(name, domain.getName());
        assertNotNull(domain.getMenu());
        assertEquals("/path", domain.getMenu().menuPath());
        assertEquals("component.vue", domain.getMenu().component());
        assertTrue(domain.getMenu().affixTab());
        assertFalse(domain.getMenu().noBasicLayout());
        assertTrue(domain.getMenu().embedded());
        assertNotNull(domain.getMenuExtendInfo());
    }

    @Test
    void handle_shouldSetCorrectMenuExtendInfo() {
        String name = "Test Menu";
        Catalog catalog = new Catalog("/redirect");
        Menu menu = new Menu("/path", "component.vue", false, false, false);
        Button button = new Button("permission", "/api");
        MenuExtendInfo menuExtendInfo = new MenuExtendInfo(1, "Title", "icon", 2, true, true);

        SysMenuDomain domain = menuStrategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertNotNull(domain.getMenuExtendInfo());
        assertEquals(1, domain.getMenuExtendInfo().order());
        assertEquals("Title", domain.getMenuExtendInfo().title());
        assertEquals("icon", domain.getMenuExtendInfo().icon());
    }
}
