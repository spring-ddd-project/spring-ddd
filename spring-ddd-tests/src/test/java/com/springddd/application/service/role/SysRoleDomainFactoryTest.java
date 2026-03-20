package com.springddd.application.service.role;

import com.springddd.domain.role.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SysRoleDomainFactoryTest {

    private final SysRoleDomainFactoryImpl factory = new SysRoleDomainFactoryImpl();

    @Test
    void newInstance_shouldCreateDomainWithCorrectValues() {
        RoleId roleId = new RoleId(1L);
        RoleBasicInfo basicInfo = new RoleBasicInfo("Admin", "ADMIN", 1, true);
        RoleExtendInfo extendInfo = new RoleExtendInfo("Administrator role", true);
        DataPermission dataPermission = new DataPermission();
        Long deptId = 100L;

        SysRoleDomain domain = factory.newInstance(roleId, basicInfo, extendInfo, dataPermission, deptId);

        assertNotNull(domain);
        assertEquals(roleId, domain.getRoleId());
        assertEquals(basicInfo, domain.getRoleBasicInfo());
        assertEquals(extendInfo, domain.getRoleExtendInfo());
        assertEquals(dataPermission, domain.getDataPermission());
        assertEquals(deptId, domain.getDeptId());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void newInstance_shouldSetDeleteStatusToFalse() {
        RoleId roleId = new RoleId(1L);
        RoleBasicInfo basicInfo = new RoleBasicInfo("Admin", "ADMIN", 1, true);
        RoleExtendInfo extendInfo = new RoleExtendInfo("desc", true);
        Long deptId = 100L;

        SysRoleDomain domain = factory.newInstance(roleId, basicInfo, extendInfo, null, deptId);

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void newInstance_shouldHandleNullRoleId() {
        RoleBasicInfo basicInfo = new RoleBasicInfo("Admin", "ADMIN", 1, true);
        RoleExtendInfo extendInfo = new RoleExtendInfo("desc", true);
        Long deptId = 100L;

        SysRoleDomain domain = factory.newInstance(null, basicInfo, extendInfo, null, deptId);

        assertNotNull(domain);
        assertNull(domain.getRoleId());
        assertEquals(basicInfo, domain.getRoleBasicInfo());
        assertEquals(extendInfo, domain.getRoleExtendInfo());
    }

    @Test
    void newInstance_shouldHandleNullBasicInfo() {
        RoleId roleId = new RoleId(1L);
        RoleExtendInfo extendInfo = new RoleExtendInfo("desc", true);
        Long deptId = 100L;

        SysRoleDomain domain = factory.newInstance(roleId, null, extendInfo, null, deptId);

        assertNotNull(domain);
        assertEquals(roleId, domain.getRoleId());
        assertNull(domain.getRoleBasicInfo());
        assertEquals(extendInfo, domain.getRoleExtendInfo());
    }

    @Test
    void newInstance_shouldHandleNullExtendInfo() {
        RoleId roleId = new RoleId(1L);
        RoleBasicInfo basicInfo = new RoleBasicInfo("Admin", "ADMIN", 1, true);
        Long deptId = 100L;

        SysRoleDomain domain = factory.newInstance(roleId, basicInfo, null, null, deptId);

        assertNotNull(domain);
        assertEquals(roleId, domain.getRoleId());
        assertEquals(basicInfo, domain.getRoleBasicInfo());
        assertNull(domain.getRoleExtendInfo());
    }

    @Test
    void newInstance_shouldHandleNullDataPermission() {
        RoleId roleId = new RoleId(1L);
        RoleBasicInfo basicInfo = new RoleBasicInfo("Admin", "ADMIN", 1, true);
        RoleExtendInfo extendInfo = new RoleExtendInfo("desc", true);
        Long deptId = 100L;

        SysRoleDomain domain = factory.newInstance(roleId, basicInfo, extendInfo, null, deptId);

        assertNotNull(domain);
        assertNull(domain.getDataPermission());
    }

    @Test
    void newInstance_shouldSetCorrectDeptId() {
        RoleId roleId = new RoleId(1L);
        Long expectedDeptId = 999L;

        SysRoleDomain domain = factory.newInstance(roleId, null, null, null, expectedDeptId);

        assertEquals(expectedDeptId, domain.getDeptId());
    }
}
