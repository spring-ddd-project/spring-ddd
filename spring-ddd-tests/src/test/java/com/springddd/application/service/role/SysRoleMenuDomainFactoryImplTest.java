package com.springddd.application.service.role;

import com.springddd.domain.menu.MenuId;
import com.springddd.domain.role.RoleId;
import com.springddd.domain.role.SysRoleMenuDomain;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysRoleMenuDomainFactoryImplTest {

    @Test
    void newInstance_shouldCreateDomainWithCorrectValues() {
        RoleId roleId = new RoleId(1L);
        MenuId menuId = new MenuId(10L);
        Long deptId = 1L;

        SysRoleMenuDomainFactoryImpl factory = new SysRoleMenuDomainFactoryImpl();
        SysRoleMenuDomain result = factory.newInstance(roleId, menuId, deptId);

        assertNotNull(result);
        assertEquals(roleId, result.getRoleId());
        assertEquals(menuId, result.getMenuId());
        assertEquals(deptId, result.getDeptId());
        assertFalse(result.getDeleteStatus());
    }

    @Test
    void newInstance_shouldSetDeleteStatusToFalse() {
        RoleId roleId = new RoleId(1L);
        MenuId menuId = new MenuId(10L);
        Long deptId = 1L;

        SysRoleMenuDomainFactoryImpl factory = new SysRoleMenuDomainFactoryImpl();
        SysRoleMenuDomain result = factory.newInstance(roleId, menuId, deptId);

        assertFalse(result.getDeleteStatus());
    }
}
