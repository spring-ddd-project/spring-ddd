package com.springddd.application.service.role;

import com.springddd.domain.menu.MenuId;
import com.springddd.domain.role.RoleId;
import com.springddd.domain.role.SysRoleMenuDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysRoleMenuDomainFactoryImplTest {

    private final SysRoleMenuDomainFactoryImpl factory = new SysRoleMenuDomainFactoryImpl();

    @Test
    @DisplayName("should create SysRoleMenuDomain with correct fields set")
    void newInstance() {
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
}
