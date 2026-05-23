package com.springddd.application.service.role;

import com.springddd.domain.role.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class SysRoleDomainFactoryImplTest {

    private final SysRoleDomainFactoryImpl factory = new SysRoleDomainFactoryImpl();

    @Test
    @DisplayName("should create SysRoleDomain with correct fields set")
    void newInstance() {
        RoleId roleId = new RoleId(1L);
        RoleBasicInfo roleBasicInfo = new RoleBasicInfo("Admin", "admin", 1, true, 1, true);
        RoleExtendInfo roleExtendInfo = new RoleExtendInfo("remark", true, "desc", true);
        DataPermission dataPermission = new DataPermission(
                new RowScope(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), false),
                Collections.emptyList(),
                1,
                Collections.emptyList()
        );
        Long deptId = 1L;

        SysRoleDomain domain = factory.newInstance(roleId, roleBasicInfo, roleExtendInfo, dataPermission, deptId);

        assertNotNull(domain);
        assertEquals(roleId, domain.getRoleId());
        assertEquals(roleBasicInfo, domain.getRoleBasicInfo());
        assertEquals(roleExtendInfo, domain.getRoleExtendInfo());
        assertEquals(dataPermission, domain.getDataPermission());
        assertEquals(deptId, domain.getDeptId());
        assertFalse(domain.getDeleteStatus());
    }
}
