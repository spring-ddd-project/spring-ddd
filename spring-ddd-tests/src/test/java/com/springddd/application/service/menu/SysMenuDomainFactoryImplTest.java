package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SysMenuDomainFactoryImplTest {

    @Mock
    private SysMenuDomainStrategy catalogStrategy;

    @Mock
    private SysMenuDomainStrategy menuStrategy;

    @Mock
    private SysMenuDomainStrategy buttonStrategy;

    private SysMenuDomainFactoryImpl sysMenuDomainFactory;

    @BeforeEach
    void setUp() {
        List<SysMenuDomainStrategy> strategies = Arrays.asList(catalogStrategy, menuStrategy, buttonStrategy);
        sysMenuDomainFactory = new SysMenuDomainFactoryImpl(strategies);
    }

    @Test
    void create_shouldUseCatalogStrategy_whenMenuTypeIsCatalog() {
        MenuId parentId = new MenuId(0L);
        String name = "Test Catalog";
        Catalog catalog = new Catalog("/redirect");
        Menu menu = new Menu("/path", "component.vue", false, false, false);
        Button button = new Button("permission", "/api");
        MenuExtendInfo menuExtendInfo = new MenuExtendInfo(1, "Title", "icon", 1, true, true);
        Long deptId = 1L;

        SysMenuDomain expectedDomain = new SysMenuDomain();
        expectedDomain.setName(name);

        when(catalogStrategy.check(1)).thenReturn(true);
        when(catalogStrategy.handle(any(), any(), any(), any(), any())).thenReturn(expectedDomain);

        SysMenuDomain result = sysMenuDomainFactory.create(parentId, name, catalog, menu, button, menuExtendInfo, deptId);

        assertNotNull(result);
        assertEquals(parentId, result.getParentId());
        assertEquals(deptId, result.getDeptId());
        assertFalse(result.getDeleteStatus());
        verify(catalogStrategy).handle(name, catalog, menu, button, menuExtendInfo);
    }

    @Test
    void create_shouldUseMenuStrategy_whenMenuTypeIsMenu() {
        MenuId parentId = new MenuId(0L);
        String name = "Test Menu";
        Catalog catalog = new Catalog("/redirect");
        Menu menu = new Menu("/path", "component.vue", false, false, false);
        Button button = new Button("permission", "/api");
        MenuExtendInfo menuExtendInfo = new MenuExtendInfo(1, "Title", "icon", 2, true, true);
        Long deptId = 1L;

        SysMenuDomain expectedDomain = new SysMenuDomain();
        expectedDomain.setName(name);

        when(menuStrategy.check(2)).thenReturn(true);
        when(menuStrategy.handle(any(), any(), any(), any(), any())).thenReturn(expectedDomain);

        SysMenuDomain result = sysMenuDomainFactory.create(parentId, name, catalog, menu, button, menuExtendInfo, deptId);

        assertNotNull(result);
        verify(menuStrategy).handle(name, catalog, menu, button, menuExtendInfo);
    }

    @Test
    void create_shouldUseButtonStrategy_whenMenuTypeIsButton() {
        MenuId parentId = new MenuId(0L);
        String name = "Test Button";
        Catalog catalog = new Catalog("/redirect");
        Menu menu = new Menu("/path", "component.vue", false, false, false);
        Button button = new Button("test:view", "/api/test");
        MenuExtendInfo menuExtendInfo = new MenuExtendInfo(1, 3, true);
        Long deptId = 1L;

        SysMenuDomain expectedDomain = new SysMenuDomain();
        expectedDomain.setName(name);

        when(buttonStrategy.check(3)).thenReturn(true);
        when(buttonStrategy.handle(any(), any(), any(), any(), any())).thenReturn(expectedDomain);

        SysMenuDomain result = sysMenuDomainFactory.create(parentId, name, catalog, menu, button, menuExtendInfo, deptId);

        assertNotNull(result);
        verify(buttonStrategy).handle(name, catalog, menu, button, menuExtendInfo);
    }

    @Test
    void create_shouldSetDeleteStatusToFalse() {
        MenuId parentId = new MenuId(0L);
        String name = "Test";
        Catalog catalog = new Catalog("/redirect");
        Menu menu = new Menu("/path", "component.vue", false, false, false);
        Button button = new Button("permission", "/api");
        MenuExtendInfo menuExtendInfo = new MenuExtendInfo(1, "Title", "icon", 1, true, true);
        Long deptId = 1L;

        SysMenuDomain expectedDomain = new SysMenuDomain();
        when(catalogStrategy.check(1)).thenReturn(true);
        when(catalogStrategy.handle(any(), any(), any(), any(), any())).thenReturn(expectedDomain);

        SysMenuDomain result = sysMenuDomainFactory.create(parentId, name, catalog, menu, button, menuExtendInfo, deptId);

        assertFalse(result.getDeleteStatus());
    }
}
