package com.springddd.application.service.role;

import com.springddd.domain.role.*;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class SysRoleDomainFactoryImplTest {

    @Test
    void shouldCreateNewInstance() {
        SysRoleDomainFactoryImpl factory = new SysRoleDomainFactoryImpl();

        RoleId roleId = new RoleId(1L);
        RoleBasicInfo basicInfo = new RoleBasicInfo("RoleName", "ROLE_CODE", 1, true);
        RoleExtendInfo extendInfo = new RoleExtendInfo("Description", true);
        DataPermission dataPermission = new DataPermission();
        dataPermission.setRowScope(new RowScope());
        dataPermission.setColumnRules(Arrays.asList());

        SysRoleDomain domain = factory.newInstance(roleId, basicInfo, extendInfo, dataPermission, 1L);

        assertNotNull(domain);
        assertEquals(roleId, domain.getRoleId());
        assertEquals(basicInfo, domain.getRoleBasicInfo());
        assertEquals(extendInfo, domain.getRoleExtendInfo());
        assertEquals(dataPermission, domain.getDataPermission());
        assertEquals(1L, domain.getDeptId());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldCreateDomainWithNullRoleId() {
        SysRoleDomainFactoryImpl factory = new SysRoleDomainFactoryImpl();

        RoleBasicInfo basicInfo = new RoleBasicInfo("RoleName", "ROLE_CODE", 1, true);
        RoleExtendInfo extendInfo = new RoleExtendInfo("Description", true);
        DataPermission dataPermission = new DataPermission();

        SysRoleDomain domain = factory.newInstance(null, basicInfo, extendInfo, dataPermission, 1L);

        assertNotNull(domain);
        assertNull(domain.getRoleId());
    }
}
