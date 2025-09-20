package com.springddd.application.service.role;

import com.springddd.application.service.role.SysRoleMenuDomainFactoryImpl;
import com.springddd.domain.menu.MenuId;
import com.springddd.domain.role.RoleId;
import com.springddd.domain.role.SysRoleMenuDomain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SysRoleMenuDomainFactoryTest {

    private final SysRoleMenuDomainFactoryImpl factory = new SysRoleMenuDomainFactoryImpl();

    @Test
    void newInstance_shouldCreateDomainWithCorrectValues() {
        RoleId roleId = new RoleId(1L);
        MenuId menuId = new MenuId(10L);
        Long deptId = 100L;

        SysRoleMenuDomain domain = factory.newInstance(roleId, menuId, deptId);

        assertNotNull(domain);
        assertEquals(roleId, domain.getRoleId());
        assertEquals(menuId, domain.getMenuId());
        assertEquals(deptId, domain.getDeptId());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void newInstance_shouldSetDeleteStatusToFalse() {
        RoleId roleId = new RoleId(1L);
        MenuId menuId = new MenuId(10L);
        Long deptId = 100L;

        SysRoleMenuDomain domain = factory.newInstance(roleId, menuId, deptId);

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void newInstance_shouldHandleNullRoleId() {
        MenuId menuId = new MenuId(10L);
        Long deptId = 100L;

        SysRoleMenuDomain domain = factory.newInstance(null, menuId, deptId);

        assertNotNull(domain);
        assertNull(domain.getRoleId());
        assertEquals(menuId, domain.getMenuId());
        assertEquals(deptId, domain.getDeptId());
    }

    @Test
    void newInstance_shouldHandleNullMenuId() {
        RoleId roleId = new RoleId(1L);
        Long deptId = 100L;

        SysRoleMenuDomain domain = factory.newInstance(roleId, null, deptId);

        assertNotNull(domain);
        assertEquals(roleId, domain.getRoleId());
        assertNull(domain.getMenuId());
        assertEquals(deptId, domain.getDeptId());
    }

    @Test
    void newInstance_shouldHandleNullDeptId() {
        RoleId roleId = new RoleId(1L);
        MenuId menuId = new MenuId(10L);

        SysRoleMenuDomain domain = factory.newInstance(roleId, menuId, null);

        assertNotNull(domain);
        assertEquals(roleId, domain.getRoleId());
        assertEquals(menuId, domain.getMenuId());
        assertNull(domain.getDeptId());
    }

    @Test
    void newInstance_shouldSetCorrectRoleId() {
        RoleId expectedRoleId = new RoleId(999L);
        MenuId menuId = new MenuId(10L);
        Long deptId = 100L;

        SysRoleMenuDomain domain = factory.newInstance(expectedRoleId, menuId, deptId);

        assertEquals(expectedRoleId, domain.getRoleId());
    }

    @Test
    void newInstance_shouldSetCorrectMenuId() {
        RoleId roleId = new RoleId(1L);
        MenuId expectedMenuId = new MenuId(999L);
        Long deptId = 100L;

        SysRoleMenuDomain domain = factory.newInstance(roleId, expectedMenuId, deptId);

        assertEquals(expectedMenuId, domain.getMenuId());
    }
}
