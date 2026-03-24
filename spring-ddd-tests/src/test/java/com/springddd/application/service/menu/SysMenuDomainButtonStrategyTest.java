package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysMenuDomainButtonStrategyTest {

    private SysMenuDomainButtonStrategy strategy;

    private String name;
    private Catalog catalog;
    private Menu menu;
    private Button button;
    private MenuExtendInfo menuExtendInfo;

    @BeforeEach
    void setUp() {
        strategy = new SysMenuDomainButtonStrategy();

        name = "Test Button";
        catalog = new Catalog("/home");
        menu = new Menu("/test", "TestComponent", true, false, false);
        button = new Button("test:view", "/api/test");
        menuExtendInfo = new MenuExtendInfo(1, 3, true);
    }

    @Test
    void check_shouldReturnTrueForType3() {
        assertTrue(strategy.check(3));
    }

    @Test
    void check_shouldReturnFalseForOtherTypes() {
        assertFalse(strategy.check(1));
        assertFalse(strategy.check(2));
        assertFalse(strategy.check(0));
    }

    @Test
    void handle_shouldCreateDomainWithButton() {
        SysMenuDomain result = strategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertNotNull(result.getButton());
        assertEquals(button.permission(), result.getButton().permission());
        assertEquals(button.api(), result.getButton().api());
    }

    @Test
    void handle_shouldCreateNewButtonInstance() {
        SysMenuDomain result = strategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertNotSame(button, result.getButton());
        assertEquals(button.permission(), result.getButton().permission());
    }

    @Test
    void handle_shouldSetCorrectMenuExtendInfo() {
        SysMenuDomain result = strategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertNotNull(result.getMenuExtendInfo());
        assertEquals(menuExtendInfo.order(), result.getMenuExtendInfo().order());
        assertEquals(menuExtendInfo.menuType(), result.getMenuExtendInfo().menuType());
        assertEquals(menuExtendInfo.menuStatus(), result.getMenuExtendInfo().menuStatus());
    }

    @Test
    void handle_shouldIgnoreCatalogParameter() {
        Catalog differentCatalog = new Catalog("/different");
        SysMenuDomain result = strategy.handle(name, differentCatalog, menu, button, menuExtendInfo);

        assertNotNull(result);
        assertNull(result.getCatalog());
    }

    @Test
    void handle_shouldIgnoreMenuParameter() {
        Menu differentMenu = new Menu("/different", "DifferentComponent", false, true, true);
        SysMenuDomain result = strategy.handle(name, catalog, differentMenu, button, menuExtendInfo);

        assertNotNull(result);
        assertNull(result.getMenu());
    }

    @Test
    void handle_shouldHandleButtonWithNullFields() {
        Button nullButton = new Button(null, null);
        SysMenuDomain result = strategy.handle(name, catalog, menu, nullButton, menuExtendInfo);

        assertNotNull(result);
        assertNotNull(result.getButton());
        assertNull(result.getButton().permission());
        assertNull(result.getButton().api());
    }
}
