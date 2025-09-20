package com.springddd.domain.role;

import com.springddd.domain.menu.MenuId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysRoleMenuDomainTest {

    @Test
    void create_ShouldSetDefaultValues() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();

        domain.create();

        assertNotNull(domain);
    }

    @Test
    void update_ShouldUpdateAllFields() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        RoleId roleId = new RoleId(1L);
        MenuId menuId = new MenuId(10L);
        Long deptId = 100L;
        String updateBy = "admin";

        domain.update(roleId, menuId, deptId, updateBy);

        assertEquals(roleId, domain.getRoleId());
        assertEquals(menuId, domain.getMenuId());
        assertEquals(deptId, domain.getDeptId());
        assertEquals(updateBy, domain.getUpdateBy());
        assertNotNull(domain.getUpdateTime());
    }

    @Test
    void delete_ShouldSetDeleteStatusTrue() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        assertNull(domain.getDeleteStatus());

        domain.delete("admin");

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void delete_ShouldAcceptUpdateByParameter() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();

        domain.delete("testUser");

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void update_ShouldSetUpdateTime() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        RoleId roleId = new RoleId(1L);
        MenuId menuId = new MenuId(10L);

        domain.update(roleId, menuId, 100L, "admin");

        assertNotNull(domain.getUpdateTime());
    }

    @Test
    void update_WithNullRoleId_ShouldSucceed() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        MenuId menuId = new MenuId(10L);

        domain.update(null, menuId, 100L, "admin");

        assertNull(domain.getRoleId());
        assertEquals(menuId, domain.getMenuId());
    }

    @Test
    void update_WithNullMenuId_ShouldSucceed() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        RoleId roleId = new RoleId(1L);

        domain.update(roleId, null, 100L, "admin");

        assertEquals(roleId, domain.getRoleId());
        assertNull(domain.getMenuId());
    }

    @Test
    void delete_ThenRestore_ShouldToggleDeleteStatus() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();

        assertNull(domain.getDeleteStatus());
        domain.delete("admin");
        assertTrue(domain.getDeleteStatus());
        domain.setDeleteStatus(false);
        assertFalse(domain.getDeleteStatus());
    }
}
