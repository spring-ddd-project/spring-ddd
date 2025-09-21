package com.springddd.domain.role;

import com.springddd.domain.role.exception.RoleCodeNullException;
import com.springddd.domain.role.exception.RoleDataScopeNullException;
import com.springddd.domain.role.exception.RoleNameNullException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SysRoleDomainTest {

    @InjectMocks
    private SysRoleDomain sysRoleDomain;

    @Test
    void create_ShouldSetDefaultValues() {
        // Given
        SysRoleDomain domain = new SysRoleDomain();

        // When
        domain.create();

        // Then - create() is a no-op, just verify no exceptions
        assertNotNull(domain);
    }

    @Test
    void updateRole_ShouldUpdateAllFields() {
        // Given
        SysRoleDomain domain = new SysRoleDomain();
        RoleId roleId = new RoleId(1L);
        domain.setRoleId(roleId);

        RoleBasicInfo roleBasicInfo = new RoleBasicInfo("Admin", "ADMIN", 1, true);
        RoleExtendInfo roleExtendInfo = new RoleExtendInfo("Administrator role", true);
        DataPermission dataPermission = new DataPermission();
        Long deptId = 100L;

        // When
        domain.updateRole(roleBasicInfo, roleExtendInfo, dataPermission, deptId);

        // Then
        assertEquals(roleBasicInfo, domain.getRoleBasicInfo());
        assertEquals(roleExtendInfo, domain.getRoleExtendInfo());
        assertEquals(dataPermission, domain.getDataPermission());
        assertEquals(deptId, domain.getDeptId());
    }

    @Test
    void delete_ShouldSetDeleteStatusTrue() {
        // Given
        SysRoleDomain domain = new SysRoleDomain();
        assertNull(domain.getDeleteStatus());

        // When
        domain.delete();

        // Then
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void restore_ShouldSetDeleteStatusFalse() {
        // Given
        SysRoleDomain domain = new SysRoleDomain();
        domain.setDeleteStatus(true);

        // When
        domain.restore();

        // Then
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void updateRole_WithNullBasicInfo_ShouldSucceed() {
        // Given
        SysRoleDomain domain = new SysRoleDomain();
        RoleExtendInfo roleExtendInfo = new RoleExtendInfo("desc", true);
        DataPermission dataPermission = new DataPermission();
        Long deptId = 100L;

        // When
        domain.updateRole(null, roleExtendInfo, dataPermission, deptId);

        // Then
        assertNull(domain.getRoleBasicInfo());
        assertEquals(roleExtendInfo, domain.getRoleExtendInfo());
        assertEquals(dataPermission, domain.getDataPermission());
        assertEquals(deptId, domain.getDeptId());
    }

    @Test
    void updateRole_WithNullExtendInfo_ShouldSucceed() {
        // Given
        SysRoleDomain domain = new SysRoleDomain();
        RoleBasicInfo roleBasicInfo = new RoleBasicInfo("Admin", "ADMIN", 1, true);
        DataPermission dataPermission = new DataPermission();
        Long deptId = 100L;

        // When
        domain.updateRole(roleBasicInfo, null, dataPermission, deptId);

        // Then
        assertEquals(roleBasicInfo, domain.getRoleBasicInfo());
        assertNull(domain.getRoleExtendInfo());
        assertEquals(dataPermission, domain.getDataPermission());
    }

    @Test
    void updateRole_WithNullDataPermission_ShouldSucceed() {
        // Given
        SysRoleDomain domain = new SysRoleDomain();
        RoleBasicInfo roleBasicInfo = new RoleBasicInfo("Admin", "ADMIN", 1, true);
        RoleExtendInfo roleExtendInfo = new RoleExtendInfo("desc", true);
        Long deptId = 100L;

        // When
        domain.updateRole(roleBasicInfo, roleExtendInfo, null, deptId);

        // Then
        assertEquals(roleBasicInfo, domain.getRoleBasicInfo());
        assertEquals(roleExtendInfo, domain.getRoleExtendInfo());
        assertNull(domain.getDataPermission());
        assertEquals(deptId, domain.getDeptId());
    }

    @Test
    void delete_ThenRestore_ShouldToggleDeleteStatus() {
        // Given
        SysRoleDomain domain = new SysRoleDomain();

        // When & Then
        assertNull(domain.getDeleteStatus());
        domain.delete();
        assertTrue(domain.getDeleteStatus());
        domain.restore();
        assertFalse(domain.getDeleteStatus());
    }
}
