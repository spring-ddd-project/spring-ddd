package com.springddd.application.service.role;

import com.springddd.domain.menu.MenuId;
import com.springddd.domain.role.RoleId;
import com.springddd.domain.role.SysRoleMenuDomain;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysRoleMenuDomainFactoryImplTest {

    @Test
    void newInstance_shouldCreateDomain() {
        SysRoleMenuDomainFactoryImpl factory = new SysRoleMenuDomainFactoryImpl();

        RoleId roleId = new RoleId(1L);
        MenuId menuId = new MenuId(2L);
        Long deptId = 1L;

        SysRoleMenuDomain domain = factory.newInstance(roleId, menuId, deptId);

        assertNotNull(domain);
        assertEquals(roleId, domain.getRoleId());
        assertEquals(menuId, domain.getMenuId());
        assertEquals(deptId, domain.getDeptId());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void newInstance_shouldCreateDomainWithNullIds() {
        SysRoleMenuDomainFactoryImpl factory = new SysRoleMenuDomainFactoryImpl();

        RoleId roleId = null;
        MenuId menuId = null;
        Long deptId = 1L;

        SysRoleMenuDomain domain = factory.newInstance(roleId, menuId, deptId);

        assertNotNull(domain);
        assertNull(domain.getRoleId());
        assertNull(domain.getMenuId());
        assertEquals(deptId, domain.getDeptId());
        assertFalse(domain.getDeleteStatus());
    }
}