package com.springddd.application.service.role;

import com.springddd.domain.role.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysRoleDomainFactoryTest {

    @Test
    void newInstance_shouldCreateDomainWithCorrectValues() {
        RoleId roleId = new RoleId(1L);
        RoleBasicInfo roleBasicInfo = new RoleBasicInfo("Test Role", "TEST_ROLE", 1, true);
        RoleExtendInfo roleExtendInfo = new RoleExtendInfo("Test Description", true);
        DataPermission dataPermission = new DataPermission();
        Long deptId = 1L;

        SysRoleDomainFactoryImpl factory = new SysRoleDomainFactoryImpl();
        SysRoleDomain result = factory.newInstance(roleId, roleBasicInfo, roleExtendInfo, dataPermission, deptId);

        assertNotNull(result);
        assertEquals(roleId, result.getRoleId());
        assertEquals(roleBasicInfo, result.getRoleBasicInfo());
        assertEquals(roleExtendInfo, result.getRoleExtendInfo());
        assertEquals(dataPermission, result.getDataPermission());
        assertEquals(deptId, result.getDeptId());
        assertFalse(result.getDeleteStatus());
    }

    @Test
    void newInstance_shouldSetDeleteStatusToFalse() {
        RoleId roleId = new RoleId(1L);
        RoleBasicInfo roleBasicInfo = new RoleBasicInfo("Test Role", "TEST_ROLE", 1, true);
        RoleExtendInfo roleExtendInfo = new RoleExtendInfo("Test Description", true);
        DataPermission dataPermission = new DataPermission();
        Long deptId = 1L;

        SysRoleDomainFactoryImpl factory = new SysRoleDomainFactoryImpl();
        SysRoleDomain result = factory.newInstance(roleId, roleBasicInfo, roleExtendInfo, dataPermission, deptId);

        assertFalse(result.getDeleteStatus());
    }
}
