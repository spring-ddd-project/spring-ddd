package com.springddd.application.service.role;

import com.springddd.domain.menu.MenuId;
import com.springddd.domain.role.RoleId;
import com.springddd.domain.role.SysRoleMenuDomain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SysRoleMenuDomainFactoryImplTest {

    private final SysRoleMenuDomainFactoryImpl factory = new SysRoleMenuDomainFactoryImpl();

    @Test
    void newInstance_shouldCreateDomainWithCorrectValues() {
        RoleId roleId = new RoleId(1L);
        MenuId menuId = new MenuId(10L);

        SysRoleMenuDomain domain = factory.newInstance(roleId, menuId, 1L);

        assertNotNull(domain);
        assertEquals(roleId, domain.getRoleId());
        assertEquals(menuId, domain.getMenuId());
        assertEquals(1L, domain.getDeptId());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void newInstance_shouldSetDeleteStatusToFalse() {
        RoleId roleId = new RoleId(1L);
        MenuId menuId = new MenuId(10L);

        SysRoleMenuDomain domain = factory.newInstance(roleId, menuId, 1L);

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void newInstance_shouldHandleNullRoleId() {
        MenuId menuId = new MenuId(10L);

        SysRoleMenuDomain domain = factory.newInstance(null, menuId, 1L);

        assertNotNull(domain);
        assertNull(domain.getRoleId());
        assertEquals(menuId, domain.getMenuId());
        assertEquals(1L, domain.getDeptId());
    }

    @Test
    void newInstance_shouldHandleNullMenuId() {
        RoleId roleId = new RoleId(1L);

        SysRoleMenuDomain domain = factory.newInstance(roleId, null, 1L);

        assertNotNull(domain);
        assertEquals(roleId, domain.getRoleId());
        assertNull(domain.getMenuId());
        assertEquals(1L, domain.getDeptId());
    }

    @Test
    void newInstance_shouldSetCorrectDeptId() {
        RoleId roleId = new RoleId(1L);
        MenuId menuId = new MenuId(10L);

        SysRoleMenuDomain domain = factory.newInstance(roleId, menuId, 100L);

        assertEquals(100L, domain.getDeptId());
    }

    @Test
    void newInstance_shouldHandleNullDeptId() {
        RoleId roleId = new RoleId(1L);
        MenuId menuId = new MenuId(10L);

        SysRoleMenuDomain domain = factory.newInstance(roleId, menuId, null);

        assertNotNull(domain);
        assertNull(domain.getDeptId());
    }
}
