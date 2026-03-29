package com.springddd.domain.role;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysRoleDomainTest {

    @Test
    void create_shouldInitializeDomain() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleId(new RoleId(1L));
        domain.setRoleBasicInfo(new RoleBasicInfo("角色A", "role_a", 1, true));
        domain.setRoleExtendInfo(new RoleExtendInfo("描述", true));
        DataPermission dataPermission = new DataPermission();
        domain.setDataPermission(dataPermission);
        domain.create();
        assertNotNull(domain);
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void updateRole_shouldModifyDomain() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleId(new RoleId(1L));

        RoleBasicInfo newBasicInfo = new RoleBasicInfo("新角色", "new_role", 2, true);
        RoleExtendInfo newExtendInfo = new RoleExtendInfo("新描述", false);
        DataPermission newDataPermission = new DataPermission();

        domain.updateRole(newBasicInfo, newExtendInfo, newDataPermission, 100L);

        assertEquals(newBasicInfo, domain.getRoleBasicInfo());
        assertEquals(newExtendInfo, domain.getRoleExtendInfo());
        assertEquals(newDataPermission, domain.getDataPermission());
        assertEquals(100L, domain.getDeptId());
    }

    @Test
    void delete_shouldSetDeleteStatus() {
        SysRoleDomain domain = new SysRoleDomain();
        assertFalse(domain.getDeleteStatus());

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void restore_shouldClearDeleteStatus() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.delete();
        assertTrue(domain.getDeleteStatus());

        domain.restore();

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void setRoleId_and_getRoleId_shouldWork() {
        SysRoleDomain domain = new SysRoleDomain();
        RoleId roleId = new RoleId(100L);
        domain.setRoleId(roleId);
        assertEquals(roleId, domain.getRoleId());
    }

    @Test
    void setRoleBasicInfo_and_getRoleBasicInfo_shouldWork() {
        SysRoleDomain domain = new SysRoleDomain();
        RoleBasicInfo basicInfo = new RoleBasicInfo("测试角色", "test_role", 1, true);
        domain.setRoleBasicInfo(basicInfo);
        assertEquals(basicInfo, domain.getRoleBasicInfo());
    }

    @Test
    void setRoleExtendInfo_and_getRoleExtendInfo_shouldWork() {
        SysRoleDomain domain = new SysRoleDomain();
        RoleExtendInfo extendInfo = new RoleExtendInfo("测试描述", true);
        domain.setRoleExtendInfo(extendInfo);
        assertEquals(extendInfo, domain.getRoleExtendInfo());
    }

    @Test
    void setDataPermission_and_getDataPermission_shouldWork() {
        SysRoleDomain domain = new SysRoleDomain();
        DataPermission dataPermission = new DataPermission();
        domain.setDataPermission(dataPermission);
        assertEquals(dataPermission, domain.getDataPermission());
    }
}
