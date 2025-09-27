package com.springddd.domain.role;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysRoleMenuDomainTest {

    @Test
    void shouldCreateSysRoleMenuDomain() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        assertNotNull(domain);
    }

    @Test
    void shouldSetAndGetId() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        RoleMenuId id = new RoleMenuId(1L);
        domain.setRoleMenuId(id);
        assertEquals(id, domain.getRoleMenuId());
    }

    @Test
    void shouldSetAndGetRoleId() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        RoleId roleId = new RoleId(1L);
        domain.setRoleId(roleId);
        assertEquals(roleId, domain.getRoleId());
    }

    @Test
    void shouldSetAndGetMenuId() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        com.springddd.domain.menu.MenuId menuId = new com.springddd.domain.menu.MenuId(1L);
        domain.setMenuId(menuId);
        assertEquals(menuId, domain.getMenuId());
    }

    @Test
    void shouldSetDeleteStatusOnDelete() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        domain.setDeleteStatus(false);
        domain.delete("admin");
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        String str = domain.toString();
        assertTrue(str.contains("SysRoleMenuDomain"));
    }
}
