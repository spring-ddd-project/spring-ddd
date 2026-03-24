package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SysMenuDomainStrategyTest {

    @Mock
    private SysMenuDomainStrategy catalogStrategy;

    @Mock
    private SysMenuDomainStrategy menuStrategy;

    @Mock
    private SysMenuDomainStrategy buttonStrategy;

    private String name;
    private Catalog catalog;
    private Menu menu;
    private Button button;
    private MenuExtendInfo menuExtendInfo;

    @BeforeEach
    void setUp() {
        name = "Test Menu";
        catalog = new Catalog("/index");
        menu = new Menu("/test", "TestComponent", true, false, false);
        button = new Button("test:view", "/api/test");
        menuExtendInfo = new MenuExtendInfo(1, "Test Title", "icon", 1, true, true);
    }

    @Test
    void catalogStrategy_shouldCheckCorrectType() {
        when(catalogStrategy.check(1)).thenReturn(true);
        when(catalogStrategy.check(2)).thenReturn(false);
        when(catalogStrategy.check(3)).thenReturn(false);

        assertTrue(catalogStrategy.check(1));
        assertFalse(catalogStrategy.check(2));
        assertFalse(catalogStrategy.check(3));
    }

    @Test
    void menuStrategy_shouldCheckCorrectType() {
        when(menuStrategy.check(1)).thenReturn(false);
        when(menuStrategy.check(2)).thenReturn(true);
        when(menuStrategy.check(3)).thenReturn(false);

        assertFalse(menuStrategy.check(1));
        assertTrue(menuStrategy.check(2));
        assertFalse(menuStrategy.check(3));
    }

    @Test
    void buttonStrategy_shouldCheckCorrectType() {
        when(buttonStrategy.check(1)).thenReturn(false);
        when(buttonStrategy.check(2)).thenReturn(false);
        when(buttonStrategy.check(3)).thenReturn(true);

        assertFalse(buttonStrategy.check(1));
        assertFalse(buttonStrategy.check(2));
        assertTrue(buttonStrategy.check(3));
    }

    @Test
    void catalogStrategy_shouldHandleReturnsDomainWithCatalog() {
        SysMenuDomain expectedDomain = new SysMenuDomain();
        expectedDomain.setName(name);
        expectedDomain.setCatalog(catalog);

        when(catalogStrategy.handle(eq(name), any(Catalog.class), any(Menu.class), any(Button.class), any(MenuExtendInfo.class)))
                .thenReturn(expectedDomain);

        SysMenuDomain result = catalogStrategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(catalog, result.getCatalog());
    }

    @Test
    void menuStrategy_shouldHandleReturnsDomainWithMenu() {
        SysMenuDomain expectedDomain = new SysMenuDomain();
        expectedDomain.setName(name);
        expectedDomain.setMenu(menu);

        when(menuStrategy.handle(eq(name), any(Catalog.class), any(Menu.class), any(Button.class), any(MenuExtendInfo.class)))
                .thenReturn(expectedDomain);

        SysMenuDomain result = menuStrategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(menu, result.getMenu());
    }

    @Test
    void buttonStrategy_shouldHandleReturnsDomainWithButton() {
        SysMenuDomain expectedDomain = new SysMenuDomain();
        expectedDomain.setName(name);
        expectedDomain.setButton(button);

        when(buttonStrategy.handle(eq(name), any(Catalog.class), any(Menu.class), any(Button.class), any(MenuExtendInfo.class)))
                .thenReturn(expectedDomain);

        SysMenuDomain result = buttonStrategy.handle(name, catalog, menu, button, menuExtendInfo);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(button, result.getButton());
    }
}
