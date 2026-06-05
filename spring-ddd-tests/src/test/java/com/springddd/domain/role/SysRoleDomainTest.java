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
    void shouldCallCreate() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.create();
        assertNotNull(domain);
    }

    @Test
    void shouldUpdateSysRoleDomain() {
        SysRoleDomain domain = new SysRoleDomain();
        RoleBasicInfo basicInfo = new RoleBasicInfo("newName", "newCode", 2, false);
        RoleExtendInfo extendInfo = new RoleExtendInfo("newDesc", true);
        DataPermission dataPermission = new DataPermission();

        domain.updateRole(basicInfo, extendInfo, dataPermission, 1L);

        assertEquals(basicInfo, domain.getRoleBasicInfo());
        assertEquals(extendInfo, domain.getRoleExtendInfo());
        assertEquals(dataPermission, domain.getDataPermission());
        assertEquals(1L, domain.getDeptId());
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

    @Test
    void shouldEnableWhenStateIsNullAndRoleStatusTrue() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleBasicInfo(new RoleBasicInfo("admin", "ROLE_ADMIN", 1, true, 1, true));

        domain.enable();

        assertNotNull(domain.getState());
    }

    @Test
    void shouldEnableWhenStateIsNullAndRoleStatusFalse() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleBasicInfo(new RoleBasicInfo("admin", "ROLE_ADMIN", 1, false, 1, true));

        domain.enable();

        assertNotNull(domain.getState());
    }

    @Test
    void shouldEnableWhenStateExists() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleBasicInfo(new RoleBasicInfo("admin", "ROLE_ADMIN", 1, true, 1, true));
        domain.setState(new com.springddd.domain.role.state.EnabledRoleState());

        domain.enable();

        assertNotNull(domain.getState());
    }

    @Test
    void shouldDisableWhenStateIsNullAndRoleStatusTrue() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleBasicInfo(new RoleBasicInfo("admin", "ROLE_ADMIN", 1, true, 1, true));

        domain.disable();

        assertNotNull(domain.getState());
    }

    @Test
    void shouldDisableWhenStateIsNullAndRoleStatusFalse() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleBasicInfo(new RoleBasicInfo("admin", "ROLE_ADMIN", 1, false, 1, true));

        domain.disable();

        assertNotNull(domain.getState());
    }

    @Test
    void shouldDisableWhenStateExists() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleBasicInfo(new RoleBasicInfo("admin", "ROLE_ADMIN", 1, true, 1, true));
        domain.setState(new com.springddd.domain.role.state.DisabledRoleState());

        domain.disable();

        assertNotNull(domain.getState());
    }

    @Test
    void shouldCloneWithNullFields() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleId(null);
        domain.setRoleBasicInfo(null);
        domain.setRoleExtendInfo(null);
        domain.setDataPermission(null);

        SysRoleDomain clone = domain.clone();

        assertNotNull(clone);
        assertNull(clone.getRoleId());
        assertNull(clone.getRoleBasicInfo());
        assertNull(clone.getRoleExtendInfo());
        assertNull(clone.getDataPermission());
    }

    @Test
    void shouldCloneWithAllFields() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleId(new RoleId(1L));
        domain.setRoleBasicInfo(new RoleBasicInfo("admin", "ROLE_ADMIN", 1, true, 1, true));
        domain.setRoleExtendInfo(new RoleExtendInfo("remark", true, "desc", true));
        domain.setDataPermission(new DataPermission());

        SysRoleDomain clone = domain.clone();

        assertNotNull(clone);
        assertEquals(1L, clone.getRoleId().value());
        assertEquals("admin", clone.getRoleBasicInfo().roleName());
        assertEquals("remark", clone.getRoleExtendInfo().roleRemark());
        assertNotNull(clone.getDataPermission());
    }

    @Test
    void shouldHandleCloneNotSupportedException() {
        SysRoleDomain domain = new SysRoleDomain() {
            @Override
            protected Object doClone() throws CloneNotSupportedException {
                throw new CloneNotSupportedException("test");
            }
        };
        assertThrows(AssertionError.class, domain::clone);
    }
}
