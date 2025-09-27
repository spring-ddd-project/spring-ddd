package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SysMenuDomainFactoryImplTest {

    @Test
    void create_shouldUseCatalogStrategy_whenMenuTypeIs1() {
        SysMenuDomainCatalogStrategy catalogStrategy = new SysMenuDomainCatalogStrategy();
        SysMenuDomainMenuStrategy menuStrategy = new SysMenuDomainMenuStrategy();
        SysMenuDomainButtonStrategy buttonStrategy = new SysMenuDomainButtonStrategy();

        List<SysMenuDomainStrategy> strategies = List.of(catalogStrategy, menuStrategy, buttonStrategy);
        SysMenuDomainFactoryImpl factory = new SysMenuDomainFactoryImpl(strategies);

        MenuId parentId = new MenuId(0L);
        String name = "TestMenu";
        Catalog catalog = new Catalog("redirect/path");
        Menu menu = new Menu("/path", "component", true, false, false);
        Button button = new Button("perm", "/api");
        MenuExtendInfo extendInfo = new MenuExtendInfo(1, "Title", 1, "icon", true, true);
        Long deptId = 1L;

        SysMenuDomain domain = factory.create(parentId, name, catalog, menu, button, extendInfo, deptId);

        assertNotNull(domain);
        assertEquals(name, domain.getName());
        assertNotNull(domain.getCatalog());
        assertEquals("redirect/path", domain.getCatalog().menuRedirect());
        assertEquals(parentId, domain.getParentId());
        assertEquals(deptId, domain.getDeptId());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void create_shouldUseMenuStrategy_whenMenuTypeIs2() {
        SysMenuDomainCatalogStrategy catalogStrategy = new SysMenuDomainCatalogStrategy();
        SysMenuDomainMenuStrategy menuStrategy = new SysMenuDomainMenuStrategy();
        SysMenuDomainButtonStrategy buttonStrategy = new SysMenuDomainButtonStrategy();

        List<SysMenuDomainStrategy> strategies = List.of(catalogStrategy, menuStrategy, buttonStrategy);
        SysMenuDomainFactoryImpl factory = new SysMenuDomainFactoryImpl(strategies);

        MenuId parentId = new MenuId(0L);
        String name = "TestMenu";
        Catalog catalog = new Catalog("redirect/path");
        Menu menu = new Menu("/path", "component", true, false, false);
        Button button = new Button("perm", "/api");
        MenuExtendInfo extendInfo = new MenuExtendInfo(1, "Title", 2, "icon", true, true);
        Long deptId = 1L;

        SysMenuDomain domain = factory.create(parentId, name, catalog, menu, button, extendInfo, deptId);

        assertNotNull(domain);
        assertEquals(name, domain.getName());
        assertNotNull(domain.getMenu());
        assertEquals("/path", domain.getMenu().menuPath());
        assertEquals(parentId, domain.getParentId());
        assertEquals(deptId, domain.getDeptId());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void create_shouldUseButtonStrategy_whenMenuTypeIs3() {
        SysMenuDomainCatalogStrategy catalogStrategy = new SysMenuDomainCatalogStrategy();
        SysMenuDomainMenuStrategy menuStrategy = new SysMenuDomainMenuStrategy();
        SysMenuDomainButtonStrategy buttonStrategy = new SysMenuDomainButtonStrategy();

        List<SysMenuDomainStrategy> strategies = List.of(catalogStrategy, menuStrategy, buttonStrategy);
        SysMenuDomainFactoryImpl factory = new SysMenuDomainFactoryImpl(strategies);

        MenuId parentId = new MenuId(0L);
        String name = "TestMenu";
        Catalog catalog = new Catalog("redirect/path");
        Menu menu = new Menu("/path", "component", true, false, false);
        Button button = new Button("perm", "/api");
        MenuExtendInfo extendInfo = new MenuExtendInfo(1, 3, true);
        Long deptId = 1L;

        SysMenuDomain domain = factory.create(parentId, name, catalog, menu, button, extendInfo, deptId);

        assertNotNull(domain);
        assertEquals(name, domain.getName());
        assertNotNull(domain.getButton());
        assertEquals("perm", domain.getButton().permission());
        assertEquals(parentId, domain.getParentId());
        assertEquals(deptId, domain.getDeptId());
        assertFalse(domain.getDeleteStatus());
    }
}