package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysMenuDomainCatalogStrategyTest {

    private SysMenuDomainCatalogStrategy strategy;

    private String name;
    private Catalog catalog;
    private Menu menu;
    private Button button;
    private MenuExtendInfo menuExtendInfo;

    @BeforeEach
    void setUp() {
        strategy = new SysMenuDomainCatalogStrategy();

        name = "Test Catalog Menu";
        catalog = new Catalog("/home");
        menu = new Menu("/test", "TestComponent", true, false, false);
        button = new Button("test:view", "/api/test");
        menuExtendInfo = new MenuExtendInfo(1, "Test Title", "icon", 1, true, true);
    }

    @Test
    void check_shouldReturnTrueForType1() {
        assertTrue(strategy.check(1));
    }

    @Test
    void check_shouldReturnFalseForOtherTypes() {
        assertFalse(strategy.check(2));
        assertFalse(strategy.check(3));
        assertFalse(strategy.check(0));
    }

    @Test
    void handle_shouldCreateDomainWithCatalog() {
        SysMenuDomain result = strategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertNotNull(result.getCatalog());
        assertEquals(catalog.menuRedirect(), result.getCatalog().menuRedirect());
    }

    @Test
    void handle_shouldSetCorrectMenuExtendInfo() {
        SysMenuDomain result = strategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertNotNull(result.getMenuExtendInfo());
        assertEquals(menuExtendInfo.order(), result.getMenuExtendInfo().order());
        assertEquals(menuExtendInfo.title(), result.getMenuExtendInfo().title());
        assertEquals(menuExtendInfo.menuType(), result.getMenuExtendInfo().menuType());
        assertEquals(menuExtendInfo.icon(), result.getMenuExtendInfo().icon());
        assertEquals(menuExtendInfo.menuStatus(), result.getMenuExtendInfo().menuStatus());
        assertEquals(menuExtendInfo.visible(), result.getMenuExtendInfo().visible());
    }

    @Test
    void handle_shouldCreateNewCatalogInstance() {
        SysMenuDomain result = strategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertNotSame(catalog, result.getCatalog());
        assertEquals(catalog.menuRedirect(), result.getCatalog().menuRedirect());
    }

    @Test
    void handle_shouldIgnoreMenuParameter() {
        Menu differentMenu = new Menu("/different", "DifferentComponent", false, true, true);
        SysMenuDomain result = strategy.handle(name, catalog, differentMenu, button, menuExtendInfo);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertNull(result.getMenu());
    }

    @Test
    void handle_shouldIgnoreButtonParameter() {
        Button differentButton = new Button("different:permission", "/api/different");
        SysMenuDomain result = strategy.handle(name, catalog, menu, differentButton, menuExtendInfo);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertNull(result.getButton());
    }
}
