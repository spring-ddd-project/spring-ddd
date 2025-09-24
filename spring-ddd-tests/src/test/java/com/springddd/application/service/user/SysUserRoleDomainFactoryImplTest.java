package com.springddd.application.service.user;

import com.springddd.domain.role.RoleId;
import com.springddd.domain.user.SysUserRoleDomain;
import com.springddd.domain.user.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysUserRoleDomainFactoryImplTest {

    @Test
    void newInstance_shouldCreateDomainWithCorrectValues() {
        UserId userId = new UserId(1L);
        RoleId roleId = new RoleId(10L);
        Long deptId = 1L;

        SysUserRoleDomainFactoryImpl factory = new SysUserRoleDomainFactoryImpl();
        SysUserRoleDomain result = factory.newInstance(userId, roleId, deptId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(roleId, result.getRoleId());
        assertEquals(deptId, result.getDeptId());
        assertFalse(result.getDeleteStatus());
    }

    @Test
    void newInstance_shouldSetDeleteStatusToFalse() {
        UserId userId = new UserId(1L);
        RoleId roleId = new RoleId(10L);
        Long deptId = 1L;

        SysUserRoleDomainFactoryImpl factory = new SysUserRoleDomainFactoryImpl();
        SysUserRoleDomain result = factory.newInstance(userId, roleId, deptId);

        assertFalse(result.getDeleteStatus());
    }
}
