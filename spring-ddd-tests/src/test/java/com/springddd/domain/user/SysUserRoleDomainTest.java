package com.springddd.domain.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysUserRoleDomainTest {

    @Test
    void shouldCreateSysUserRoleDomain() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        assertNotNull(domain);
    }

    @Test
    void shouldSetAndGetId() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        UserRoleId id = new UserRoleId(1L);
        domain.setUserRoleId(id);
        assertEquals(id, domain.getUserRoleId());
    }

    @Test
    void shouldSetAndGetUserId() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        UserId userId = new UserId(1L);
        domain.setUserId(userId);
        assertEquals(userId, domain.getUserId());
    }

    @Test
    void shouldSetAndGetRoleId() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        com.springddd.domain.role.RoleId roleId = new com.springddd.domain.role.RoleId(1L);
        domain.setRoleId(roleId);
        assertEquals(roleId, domain.getRoleId());
    }

    @Test
    void shouldSetDeleteStatusOnDelete() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        domain.setDeleteStatus(false);
        domain.delete();
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        String str = domain.toString();
        assertTrue(str.contains("SysUserRoleDomain"));
    }
}
