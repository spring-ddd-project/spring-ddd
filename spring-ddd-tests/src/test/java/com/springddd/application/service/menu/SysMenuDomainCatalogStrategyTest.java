package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysMenuDomainCatalogStrategyTest {

    private SysMenuDomainCatalogStrategy catalogStrategy;

    @BeforeEach
    void setUp() {
        catalogStrategy = new SysMenuDomainCatalogStrategy();
    }

    @Test
    void check_shouldReturnTrue_whenMenuTypeIsCatalog() {
        assertTrue(catalogStrategy.check(1));
    }

    @Test
    void check_shouldReturnFalse_whenMenuTypeIsNotCatalog() {
        assertFalse(catalogStrategy.check(2));
        assertFalse(catalogStrategy.check(3));
        assertFalse(catalogStrategy.check(0));
    }

    @Test
    void handle_shouldCreateDomainWithCatalogInfo() {
        String name = "Test Catalog";
        Catalog catalog = new Catalog("/redirect");
        Menu menu = new Menu("/path", "component.vue", false, false, false);
        Button button = new Button("permission", "/api");
        MenuExtendInfo menuExtendInfo = new MenuExtendInfo(1, "Title", "icon", 1, true, true);

        SysMenuDomain domain = catalogStrategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertEquals(name, domain.getName());
        assertNotNull(domain.getCatalog());
        assertEquals("/redirect", domain.getCatalog().menuRedirect());
        assertNotNull(domain.getMenuExtendInfo());
    }

    @Test
    void handle_shouldSetCorrectMenuExtendInfo() {
        String name = "Test Catalog";
        Catalog catalog = new Catalog("/redirect");
        Menu menu = new Menu("/path", "component.vue", false, false, false);
        Button button = new Button("permission", "/api");
        MenuExtendInfo menuExtendInfo = new MenuExtendInfo(1, "Title", "icon", 1, true, true);

        SysMenuDomain domain = catalogStrategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertNotNull(domain.getMenuExtendInfo());
        assertEquals(1, domain.getMenuExtendInfo().order());
        assertEquals("Title", domain.getMenuExtendInfo().title());
        assertEquals("icon", domain.getMenuExtendInfo().icon());
    }
}
