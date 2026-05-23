package com.springddd.application.service.user;

import com.springddd.domain.role.RoleId;
import com.springddd.domain.user.SysUserRoleDomain;
import com.springddd.domain.user.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysUserRoleDomainFactoryImplTest {

    private final SysUserRoleDomainFactoryImpl factory = new SysUserRoleDomainFactoryImpl();

    @Test
    @DisplayName("should create SysUserRoleDomain with correct fields set")
    void newInstance() {
        UserId userId = new UserId(1L);
        RoleId roleId = new RoleId(2L);
        Long deptId = 1L;

        SysUserRoleDomain domain = factory.newInstance(userId, roleId, deptId);

        assertNotNull(domain);
        assertEquals(userId, domain.getUserId());
        assertEquals(roleId, domain.getRoleId());
        assertEquals(deptId, domain.getDeptId());
        assertFalse(domain.getDeleteStatus());
    }
}
