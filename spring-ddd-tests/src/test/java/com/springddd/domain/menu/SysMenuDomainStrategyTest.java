package com.springddd.domain.menu;

import com.springddd.application.service.menu.SysMenuDomainButtonStrategy;
import com.springddd.application.service.menu.SysMenuDomainCatalogStrategy;
import com.springddd.application.service.menu.SysMenuDomainMenuStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysMenuDomainStrategyTest {

    private SysMenuDomainCatalogStrategy catalogStrategy;
    private SysMenuDomainMenuStrategy menuStrategy;
    private SysMenuDomainButtonStrategy buttonStrategy;

    private String name;
    private Catalog catalog;
    private Menu menu;
    private Button button;
    private MenuExtendInfo menuExtendInfo;

    @BeforeEach
    void setUp() {
        catalogStrategy = new SysMenuDomainCatalogStrategy();
        menuStrategy = new SysMenuDomainMenuStrategy();
        buttonStrategy = new SysMenuDomainButtonStrategy();

        name = "Test Menu";
        catalog = new Catalog("/index");
        menu = new Menu("/test", "TestComponent", true, false, false);
        button = new Button("test:view", "/api/test");
        menuExtendInfo = new MenuExtendInfo(1, "Test Title", "icon", 1, true, true);
    }

    @Test
    void catalogStrategy_check_shouldReturnTrueForType1() {
        assertTrue(catalogStrategy.check(1));
        assertFalse(catalogStrategy.check(2));
        assertFalse(catalogStrategy.check(3));
    }

    @Test
    void menuStrategy_check_shouldReturnTrueForType2() {
        assertFalse(menuStrategy.check(1));
        assertTrue(menuStrategy.check(2));
        assertFalse(menuStrategy.check(3));
    }

    @Test
    void buttonStrategy_check_shouldReturnTrueForType3() {
        assertFalse(buttonStrategy.check(1));
        assertFalse(buttonStrategy.check(2));
        assertTrue(buttonStrategy.check(3));
    }

    @Test
    void catalogStrategy_handle_shouldCreateDomainWithCatalog() {
        SysMenuDomain result = catalogStrategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertNotNull(result.getCatalog());
        assertEquals("/index", result.getCatalog().menuRedirect());
    }

    @Test
    void menuStrategy_handle_shouldCreateDomainWithMenu() {
        SysMenuDomain result = menuStrategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertNotNull(result.getMenu());
        assertEquals("/test", result.getMenu().menuPath());
        assertEquals("TestComponent", result.getMenu().component());
    }

    @Test
    void buttonStrategy_handle_shouldCreateDomainWithButton() {
        SysMenuDomain result = buttonStrategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertNotNull(result.getButton());
        assertEquals("test:view", result.getButton().permission());
        assertEquals("/api/test", result.getButton().api());
    }
}
