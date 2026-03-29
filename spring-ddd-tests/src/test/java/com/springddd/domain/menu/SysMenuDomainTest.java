package com.springddd.domain.menu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysMenuDomainTest {

    @Test
    void create_shouldInitializeDomain() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(new MenuId(1L));
        domain.create();
        assertNotNull(domain);
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void update_shouldModifyDomain() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(new MenuId(1L));

        MenuId newParentId = new MenuId(0L);
        String newName = "新菜单";
        Catalog catalog = new Catalog("/redirect");
        Menu menu = new Menu("/path", "/component.vue", false, false, false);
        Button button = new Button("permission:edit", "/api/edit");
        MenuExtendInfo menuExtendInfo = new MenuExtendInfo(1, "标题", "icon", 1, true, true);

        domain.update(newParentId, newName, catalog, menu, button, menuExtendInfo, 100L);

        assertEquals(newParentId, domain.getParentId());
        assertEquals(newName, domain.getName());
        assertEquals(catalog, domain.getCatalog());
        assertEquals(menu, domain.getMenu());
        assertEquals(button, domain.getButton());
        assertEquals(menuExtendInfo, domain.getMenuExtendInfo());
        assertEquals(100L, domain.getDeptId());
    }

    @Test
    void delete_shouldSetDeleteStatus() {
        SysMenuDomain domain = new SysMenuDomain();
        assertFalse(domain.getDeleteStatus());

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void restore_shouldClearDeleteStatus() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.delete();
        assertTrue(domain.getDeleteStatus());

        domain.restore();

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void setMenuId_and_getMenuId_shouldWork() {
        SysMenuDomain domain = new SysMenuDomain();
        MenuId menuId = new MenuId(100L);
        domain.setMenuId(menuId);
        assertEquals(menuId, domain.getMenuId());
    }

    @Test
    void setParentId_and_getParentId_shouldWork() {
        SysMenuDomain domain = new SysMenuDomain();
        MenuId parentId = new MenuId(50L);
        domain.setParentId(parentId);
        assertEquals(parentId, domain.getParentId());
    }

    @Test
    void setName_and_getName_shouldWork() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setName("测试菜单");
        assertEquals("测试菜单", domain.getName());
    }

    @Test
    void setCatalog_and_getCatalog_shouldWork() {
        SysMenuDomain domain = new SysMenuDomain();
        Catalog catalog = new Catalog("/redirect");
        domain.setCatalog(catalog);
        assertEquals(catalog, domain.getCatalog());
    }

    @Test
    void setMenu_and_getMenu_shouldWork() {
        SysMenuDomain domain = new SysMenuDomain();
        Menu menu = new Menu("/path", "/component.vue", false, false, false);
        domain.setMenu(menu);
        assertEquals(menu, domain.getMenu());
    }

    @Test
    void setButton_and_getButton_shouldWork() {
        SysMenuDomain domain = new SysMenuDomain();
        Button button = new Button("permission:edit", "/api/edit");
        domain.setButton(button);
        assertEquals(button, domain.getButton());
    }

    @Test
    void setAdvancedOptions_and_getAdvancedOptions_shouldWork() {
        SysMenuDomain domain = new SysMenuDomain();
        AdvancedOptions options = new AdvancedOptions(1, "icon", 1, true, true);
        domain.setAdvancedOptions(options);
        assertEquals(options, domain.getAdvancedOptions());
    }

    @Test
    void setMenuExtendInfo_and_getMenuExtendInfo_shouldWork() {
        SysMenuDomain domain = new SysMenuDomain();
        MenuExtendInfo extendInfo = new MenuExtendInfo(1, "标题", "icon", 1, true, true);
        domain.setMenuExtendInfo(extendInfo);
        assertEquals(extendInfo, domain.getMenuExtendInfo());
    }
}
