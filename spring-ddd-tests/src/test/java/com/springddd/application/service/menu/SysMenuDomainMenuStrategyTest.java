package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysMenuDomainMenuStrategyTest {

    private SysMenuDomainMenuStrategy strategy;

    private String name;
    private Catalog catalog;
    private Menu menu;
    private Button button;
    private MenuExtendInfo menuExtendInfo;

    @BeforeEach
    void setUp() {
        strategy = new SysMenuDomainMenuStrategy();

        name = "Test Menu Page";
        catalog = new Catalog("/home");
        menu = new Menu("/test", "TestComponent", true, false, false);
        button = new Button("test:view", "/api/test");
        menuExtendInfo = new MenuExtendInfo(1, "Test Title", "icon", 2, true, true);
    }

    @Test
    void check_shouldReturnTrueForType2() {
        assertTrue(strategy.check(2));
    }

    @Test
    void check_shouldReturnFalseForOtherTypes() {
        assertFalse(strategy.check(1));
        assertFalse(strategy.check(3));
        assertFalse(strategy.check(0));
    }

    @Test
    void handle_shouldCreateDomainWithMenu() {
        SysMenuDomain result = strategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertNotNull(result.getMenu());
        assertEquals(menu.menuPath(), result.getMenu().menuPath());
        assertEquals(menu.component(), result.getMenu().component());
        assertEquals(menu.affixTab(), result.getMenu().affixTab());
        assertEquals(menu.noBasicLayout(), result.getMenu().noBasicLayout());
        assertEquals(menu.embedded(), result.getMenu().embedded());
    }

    @Test
    void handle_shouldSetCorrectMenuExtendInfo() {
        SysMenuDomain result = strategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertNotNull(result.getMenuExtendInfo());
        assertEquals(menuExtendInfo.order(), result.getMenuExtendInfo().order());
        assertEquals(menuExtendInfo.title(), result.getMenuExtendInfo().title());
        assertEquals(menuExtendInfo.icon(), result.getMenuExtendInfo().icon());
        assertEquals(menuExtendInfo.menuType(), result.getMenuExtendInfo().menuType());
        assertEquals(menuExtendInfo.visible(), result.getMenuExtendInfo().visible());
        assertEquals(menuExtendInfo.menuStatus(), result.getMenuExtendInfo().menuStatus());
    }

    @Test
    void handle_shouldCreateNewMenuInstance() {
        SysMenuDomain result = strategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertNotSame(menu, result.getMenu());
        assertEquals(menu.menuPath(), result.getMenu().menuPath());
    }

    @Test
    void handle_shouldIgnoreCatalogParameter() {
        Catalog differentCatalog = new Catalog("/different");
        SysMenuDomain result = strategy.handle(name, differentCatalog, menu, button, menuExtendInfo);

        assertNotNull(result);
        assertNull(result.getCatalog());
    }

    @Test
    void handle_shouldIgnoreButtonParameter() {
        Button differentButton = new Button("different:permission", "/api/different");
        SysMenuDomain result = strategy.handle(name, catalog, menu, differentButton, menuExtendInfo);

        assertNotNull(result);
        assertNull(result.getButton());
    }

    @Test
    void handle_shouldHandleMenuWithAllNullFields() {
        Menu nullMenu = new Menu(null, null, null, null, null);
        SysMenuDomain result = strategy.handle(name, catalog, nullMenu, button, menuExtendInfo);

        assertNotNull(result);
        assertNotNull(result.getMenu());
        assertNull(result.getMenu().menuPath());
        assertNull(result.getMenu().component());
    }
}
