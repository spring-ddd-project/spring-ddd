package com.springddd.domain.user;

import com.springddd.domain.role.RoleId;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysUserRoleDomainTest {

    @Test
    void create_shouldInitializeDomain() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        domain.setId(new UserRoleId(1L));
        domain.setUserRoleId(new UserRoleId(1L));
        domain.setUserId(new UserId(1L));
        domain.setRoleId(new RoleId(1L));
        domain.create();

        assertNotNull(domain);
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void update_shouldModifyUserIdAndRoleId() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        domain.setId(new UserRoleId(1L));
        domain.setUserRoleId(new UserRoleId(1L));

        UserId newUserId = new UserId(10L);
        RoleId newRoleId = new RoleId(20L);

        domain.update(newUserId, newRoleId, 100L);

        assertEquals(newUserId, domain.getUserId());
        assertEquals(newRoleId, domain.getRoleId());
        assertEquals(100L, domain.getDeptId());
    }

    @Test
    void delete_shouldSetDeleteStatus() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        assertFalse(domain.getDeleteStatus());

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void setId_and_getId_shouldWork() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        UserRoleId id = new UserRoleId(100L);
        domain.setId(id);
        assertEquals(id, domain.getId());
    }

    @Test
    void setUserRoleId_and_getUserRoleId_shouldWork() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        UserRoleId userRoleId = new UserRoleId(50L);
        domain.setUserRoleId(userRoleId);
        assertEquals(userRoleId, domain.getUserRoleId());
    }

    @Test
    void setUserId_and_getUserId_shouldWork() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        UserId userId = new UserId(25L);
        domain.setUserId(userId);
        assertEquals(userId, domain.getUserId());
    }

    @Test
    void setRoleId_and_getRoleId_shouldWork() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        RoleId roleId = new RoleId(30L);
        domain.setRoleId(roleId);
        assertEquals(roleId, domain.getRoleId());
    }

    @Test
    void setDeptId_and_getDeptId_shouldWork() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        domain.setDeptId(200L);
        assertEquals(200L, domain.getDeptId());
    }
}
