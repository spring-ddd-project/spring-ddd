package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysMenuDomainButtonStrategyTest {

    private SysMenuDomainButtonStrategy buttonStrategy;

    @BeforeEach
    void setUp() {
        buttonStrategy = new SysMenuDomainButtonStrategy();
    }

    @Test
    void check_shouldReturnTrue_whenMenuTypeIsButton() {
        assertTrue(buttonStrategy.check(3));
    }

    @Test
    void check_shouldReturnFalse_whenMenuTypeIsNotButton() {
        assertFalse(buttonStrategy.check(1));
        assertFalse(buttonStrategy.check(2));
        assertFalse(buttonStrategy.check(0));
    }

    @Test
    void handle_shouldCreateDomainWithButtonInfo() {
        String name = "Test Button";
        Catalog catalog = new Catalog("/redirect");
        Menu menu = new Menu("/path", "component.vue", false, false, false);
        Button button = new Button("test:view", "/api/test");
        MenuExtendInfo menuExtendInfo = new MenuExtendInfo(1, 3, true);

        SysMenuDomain domain = buttonStrategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertEquals(name, domain.getName());
        assertNotNull(domain.getButton());
        assertEquals("test:view", domain.getButton().permission());
        assertEquals("/api/test", domain.getButton().api());
        assertNotNull(domain.getMenuExtendInfo());
    }

    @Test
    void handle_shouldSetCorrectMenuExtendInfo() {
        String name = "Test Button";
        Catalog catalog = new Catalog("/redirect");
        Menu menu = new Menu("/path", "component.vue", false, false, false);
        Button button = new Button("test:view", "/api/test");
        MenuExtendInfo menuExtendInfo = new MenuExtendInfo(1, 3, true);

        SysMenuDomain domain = buttonStrategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertNotNull(domain.getMenuExtendInfo());
        assertEquals(1, domain.getMenuExtendInfo().order());
        assertEquals(3, domain.getMenuExtendInfo().menuType());
        assertTrue(domain.getMenuExtendInfo().menuStatus());
    }
}
