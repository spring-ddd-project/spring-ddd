package com.springddd.domain.role;

import com.springddd.domain.menu.MenuId;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class SysRoleMenuDomainTest {

    @Test
    void create_shouldInitializeDomain() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        domain.setRoleMenuId(new RoleMenuId(1L));
        domain.setRoleId(new RoleId(1L));
        domain.setMenuId(new MenuId(1L));
        domain.create();
        assertNotNull(domain);
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void update_shouldModifyDomain() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        domain.setRoleMenuId(new RoleMenuId(1L));

        RoleId newRoleId = new RoleId(2L);
        MenuId newMenuId = new MenuId(3L);

        domain.update(newRoleId, newMenuId, 100L, "admin");

        assertEquals(newRoleId, domain.getRoleId());
        assertEquals(newMenuId, domain.getMenuId());
        assertEquals(100L, domain.getDeptId());
        assertEquals("admin", domain.getUpdateBy());
        assertNotNull(domain.getUpdateTime());
    }

    @Test
    void delete_shouldSetDeleteStatus() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        assertFalse(domain.getDeleteStatus());

        domain.delete("admin");

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void setRoleMenuId_and_getRoleMenuId_shouldWork() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        RoleMenuId roleMenuId = new RoleMenuId(100L);
        domain.setRoleMenuId(roleMenuId);
        assertEquals(roleMenuId, domain.getRoleMenuId());
    }

    @Test
    void setRoleId_and_getRoleId_shouldWork() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        RoleId roleId = new RoleId(50L);
        domain.setRoleId(roleId);
        assertEquals(roleId, domain.getRoleId());
    }

    @Test
    void setMenuId_and_getMenuId_shouldWork() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        MenuId menuId = new MenuId(75L);
        domain.setMenuId(menuId);
        assertEquals(menuId, domain.getMenuId());
    }
}
