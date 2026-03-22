package com.springddd.domain.menu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysMenuDomainTest {

    @Test
    void shouldCreateSysMenuDomainInstance() {
        SysMenuDomain domain = new SysMenuDomain();
        assertNotNull(domain);
    }

    @Test
    void shouldSetMenuId() {
        SysMenuDomain domain = new SysMenuDomain();
        MenuId menuId = new MenuId(1L);
        domain.setMenuId(menuId);
        assertEquals(menuId, domain.getMenuId());
    }

    @Test
    void shouldSetParentId() {
        SysMenuDomain domain = new SysMenuDomain();
        MenuId parentId = new MenuId(0L);
        domain.setParentId(parentId);
        assertEquals(parentId, domain.getParentId());
    }

    @Test
    void shouldSetName() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setName("Dashboard");
        assertEquals("Dashboard", domain.getName());
    }

    @Test
    void shouldSetCatalog() {
        SysMenuDomain domain = new SysMenuDomain();
        Catalog catalog = new Catalog("/index");
        domain.setCatalog(catalog);
        assertEquals(catalog, domain.getCatalog());
    }

    @Test
    void shouldSetMenu() {
        SysMenuDomain domain = new SysMenuDomain();
        Menu menu = new Menu("/user", "UserView", true, false, false);
        domain.setMenu(menu);
        assertEquals(menu, domain.getMenu());
    }

    @Test
    void shouldSetButton() {
        SysMenuDomain domain = new SysMenuDomain();
        Button button = new Button("user:create", "/api/user");
        domain.setButton(button);
        assertEquals(button, domain.getButton());
    }

    @Test
    void shouldSetAdvancedOptions() {
        SysMenuDomain domain = new SysMenuDomain();
        AdvancedOptions options = new AdvancedOptions(1, "setting", 1, true, true);
        domain.setAdvancedOptions(options);
        assertEquals(options, domain.getAdvancedOptions());
    }

    @Test
    void shouldSetMenuExtendInfo() {
        SysMenuDomain domain = new SysMenuDomain();
        MenuExtendInfo extendInfo = new MenuExtendInfo(1, "Title", "icon", 1, true, true);
        domain.setMenuExtendInfo(extendInfo);
        assertEquals(extendInfo, domain.getMenuExtendInfo());
    }

    @Test
    void shouldCallCreateMethod() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.create();
        assertNotNull(domain);
    }

    @Test
    void shouldUpdateAllFieldsInUpdateMethod() {
        SysMenuDomain domain = new SysMenuDomain();
        MenuId parentId = new MenuId(1L);
        String name = "Dashboard";
        Catalog catalog = new Catalog("/index");
        Menu menu = new Menu("/dash", "DashView", true, false, false);
        Button button = new Button("dash:view", "/api/dash");
        MenuExtendInfo extendInfo = new MenuExtendInfo(1, "Title", "icon", 1, true, true);
        Long deptId = 100L;

        domain.update(parentId, name, catalog, menu, button, extendInfo, deptId);

        assertEquals(parentId, domain.getParentId());
        assertEquals(name, domain.getName());
        assertEquals(catalog, domain.getCatalog());
        assertEquals(menu, domain.getMenu());
        assertEquals(button, domain.getButton());
        assertEquals(extendInfo, domain.getMenuExtendInfo());
        assertEquals(deptId, domain.getDeptId());
    }

    @Test
    void shouldSetDeleteStatusToTrueOnDelete() {
        SysMenuDomain domain = new SysMenuDomain();
        assertNull(domain.getDeleteStatus());
        domain.delete();
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void shouldSetDeleteStatusToFalseOnRestore() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.delete();
        assertTrue(domain.getDeleteStatus());
        domain.restore();
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldAllowUpdateWithNullParentId() {
        SysMenuDomain domain = new SysMenuDomain();
        Menu menu = new Menu("/dash", "DashView", true, false, false);
        MenuExtendInfo extendInfo = new MenuExtendInfo(1, "Title", "icon", 1, true, true);

        domain.update(null, "Dashboard", null, menu, null, extendInfo, 100L);

        assertNull(domain.getParentId());
        assertEquals("Dashboard", domain.getName());
    }

    @Test
    void shouldAllowUpdateWithNullMenuAndButton() {
        SysMenuDomain domain = new SysMenuDomain();
        MenuId parentId = new MenuId(1L);
        MenuExtendInfo extendInfo = new MenuExtendInfo(1, "Title", "icon", 1, true, true);

        domain.update(parentId, "Dashboard", null, null, null, extendInfo, 100L);

        assertEquals(parentId, domain.getParentId());
        assertNull(domain.getMenu());
        assertNull(domain.getButton());
    }
}
