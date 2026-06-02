package com.springddd.domain.menu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysMenuDomainTest {

    @Test
    void shouldCreateSysMenuDomain() {
        SysMenuDomain domain = new SysMenuDomain();
        assertNotNull(domain);
    }

    @Test
    void shouldSetAndGetId() {
        SysMenuDomain domain = new SysMenuDomain();
        MenuId id = new MenuId(1L);
        domain.setMenuId(id);
        assertEquals(id, domain.getMenuId());
    }

    @Test
    void shouldSetAndGetName() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setName("Test Menu");
        assertEquals("Test Menu", domain.getName());
    }

    @Test
    void shouldSetAndGetDeleteStatus() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setDeleteStatus(false);
        domain.delete();
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void shouldClearDeleteStatusOnRestore() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setDeleteStatus(true);
        domain.restore();
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldCallCreate() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.create();
        assertNotNull(domain);
    }

    @Test
    void shouldUpdateSysMenuDomain() {
        SysMenuDomain domain = new SysMenuDomain();
        MenuId parentId = new MenuId(0L);
        Catalog catalog = new Catalog("/dashboard");
        Menu menu = new Menu("/path", "component", true, false, false);
        Button button = new Button("perm:read", "/api/read");
        MenuExtendInfo menuExtendInfo = new MenuExtendInfo(1, "title", "icon", 1, true, true);

        domain.update(parentId, "Test Menu", catalog, menu, button, menuExtendInfo, 1L);

        assertEquals(parentId, domain.getParentId());
        assertEquals("Test Menu", domain.getName());
        assertEquals(catalog, domain.getCatalog());
        assertEquals(menu, domain.getMenu());
        assertEquals(button, domain.getButton());
        assertEquals(menuExtendInfo, domain.getMenuExtendInfo());
        assertEquals(1L, domain.getDeptId());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        SysMenuDomain domain = new SysMenuDomain();
        String str = domain.toString();
        assertTrue(str.contains("SysMenuDomain"));
    }
}
