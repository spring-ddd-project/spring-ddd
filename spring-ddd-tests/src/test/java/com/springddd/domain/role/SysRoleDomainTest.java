package com.springddd.domain.role;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysRoleDomainTest {

    @Test
    void shouldCreateSysRoleDomain() {
        SysRoleDomain domain = new SysRoleDomain();
        assertNotNull(domain);
    }

    @Test
    void shouldSetAndGetId() {
        SysRoleDomain domain = new SysRoleDomain();
        RoleId id = new RoleId(1L);
        domain.setRoleId(id);
        assertEquals(id, domain.getRoleId());
    }

    @Test
    void shouldSetAndGetBasicInfo() {
        SysRoleDomain domain = new SysRoleDomain();
        RoleBasicInfo basicInfo = new RoleBasicInfo("roleName", "roleCode", 1, true);
        domain.setRoleBasicInfo(basicInfo);
        assertEquals(basicInfo, domain.getRoleBasicInfo());
    }

    @Test
    void shouldSetAndGetDeleteStatus() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setDeleteStatus(false);
        domain.delete();
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void shouldClearDeleteStatusOnRestore() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setDeleteStatus(true);
        domain.restore();
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        SysRoleDomain domain = new SysRoleDomain();
        String str = domain.toString();
        assertTrue(str.contains("SysRoleDomain"));
    }
}
