package com.springddd.application.service.user;

import com.springddd.domain.role.RoleId;
import com.springddd.domain.user.SysUserRoleDomain;
import com.springddd.domain.user.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SysUserRoleDomainFactoryImplTest {

    private final SysUserRoleDomainFactoryImpl factory = new SysUserRoleDomainFactoryImpl();

    @Test
    void newInstance_shouldCreateDomainWithCorrectValues() {
        UserId userId = new UserId(1L);
        RoleId roleId = new RoleId(10L);
        Long deptId = 1L;

        SysUserRoleDomain domain = factory.newInstance(userId, roleId, deptId);

        assertNotNull(domain);
        assertEquals(userId, domain.getUserId());
        assertEquals(roleId, domain.getRoleId());
        assertEquals(deptId, domain.getDeptId());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void newInstance_shouldSetDeleteStatusToFalse() {
        UserId userId = new UserId(1L);
        RoleId roleId = new RoleId(10L);
        Long deptId = 1L;

        SysUserRoleDomain domain = factory.newInstance(userId, roleId, deptId);

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void newInstance_shouldHandleNullUserId() {
        RoleId roleId = new RoleId(10L);
        Long deptId = 1L;

        SysUserRoleDomain domain = factory.newInstance(null, roleId, deptId);

        assertNotNull(domain);
        assertNull(domain.getUserId());
        assertEquals(roleId, domain.getRoleId());
        assertEquals(deptId, domain.getDeptId());
    }

    @Test
    void newInstance_shouldHandleNullRoleId() {
        UserId userId = new UserId(1L);
        Long deptId = 1L;

        SysUserRoleDomain domain = factory.newInstance(userId, null, deptId);

        assertNotNull(domain);
        assertEquals(userId, domain.getUserId());
        assertNull(domain.getRoleId());
        assertEquals(deptId, domain.getDeptId());
    }

    @Test
    void newInstance_shouldSetCorrectDeptId() {
        UserId userId = new UserId(1L);
        RoleId roleId = new RoleId(10L);
        Long deptId = 100L;

        SysUserRoleDomain domain = factory.newInstance(userId, roleId, deptId);

        assertEquals(deptId, domain.getDeptId());
    }

    @Test
    void newInstance_shouldHandleNullDeptId() {
        UserId userId = new UserId(1L);
        RoleId roleId = new RoleId(10L);

        SysUserRoleDomain domain = factory.newInstance(userId, roleId, null);

        assertNotNull(domain);
        assertNull(domain.getDeptId());
    }
}
