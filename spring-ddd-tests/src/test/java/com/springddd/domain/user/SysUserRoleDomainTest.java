package com.springddd.domain.user;

import com.springddd.domain.role.RoleId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SysUserRoleDomainTest {

    @Test
    void create_shouldSetInitialState() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        UserId userId = new UserId(1L);
        RoleId roleId = new RoleId(1L);
        domain.setUserId(userId);
        domain.setRoleId(roleId);
        domain.setDeptId(100L);

        assertEquals(userId, domain.getUserId());
        assertEquals(roleId, domain.getRoleId());
        assertEquals(100L, domain.getDeptId());
    }

    @Test
    void setUserRoleId_shouldSetCorrectValue() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        UserRoleId userRoleId = new UserRoleId(1L);
        domain.setUserRoleId(userRoleId);
        assertEquals(userRoleId, domain.getUserRoleId());
    }

    @Test
    void setUserId_shouldSetCorrectValue() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        UserId userId = new UserId(5L);
        domain.setUserId(userId);
        assertEquals(userId, domain.getUserId());
    }

    @Test
    void setRoleId_shouldSetCorrectValue() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        RoleId roleId = new RoleId(10L);
        domain.setRoleId(roleId);
        assertEquals(roleId, domain.getRoleId());
    }

    @Test
    void setDeleteStatus_shouldSetCorrectValue() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        domain.setDeleteStatus(true);
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void create_shouldInitializeCorrectly() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        domain.setUserRoleId(new UserRoleId(1L));
        domain.setUserId(new UserId(1L));
        domain.setRoleId(new RoleId(1L));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);

        assertNotNull(domain.getUserRoleId());
        assertNotNull(domain.getUserId());
        assertNotNull(domain.getRoleId());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void update_shouldModifyUserIdAndRoleIdAndDeptId() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);

        UserId newUserId = new UserId(2L);
        RoleId newRoleId = new RoleId(3L);
        Long newDeptId = 200L;

        domain.update(newUserId, newRoleId, newDeptId);

        assertEquals(newUserId, domain.getUserId());
        assertEquals(newRoleId, domain.getRoleId());
        assertEquals(newDeptId, domain.getDeptId());
    }

    @Test
    void delete_shouldSetDeleteStatusToTrue() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        assertNull(domain.getDeleteStatus());

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void delete_shouldWorkOnAlreadyDeletedDomain() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        domain.setDeleteStatus(true);

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }
}
