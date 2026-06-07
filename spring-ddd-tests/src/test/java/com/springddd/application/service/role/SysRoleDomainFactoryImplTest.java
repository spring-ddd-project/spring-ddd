package com.springddd.application.service.role;

import com.springddd.domain.role.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysRoleDomainFactoryImplTest {

    @Test
    void shouldCreateNewInstance() {
        SysRoleDomainFactoryImpl factory = new SysRoleDomainFactoryImpl();

        RoleId roleId = new RoleId(1L);
        RoleBasicInfo basicInfo = new RoleBasicInfo("RoleName", "ROLE_CODE", true);
        RoleExtendInfo extendInfo = new RoleExtendInfo("Description", true);

        SysRoleDomain domain = factory.newInstance(roleId, basicInfo, extendInfo, 1L);

        assertNotNull(domain);
        assertEquals(roleId, domain.getRoleId());
        assertEquals(basicInfo, domain.getRoleBasicInfo());
        assertEquals(extendInfo, domain.getRoleExtendInfo());
        assertEquals(1L, domain.getDeptId());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldCreateDomainWithNullRoleId() {
        SysRoleDomainFactoryImpl factory = new SysRoleDomainFactoryImpl();

        RoleBasicInfo basicInfo = new RoleBasicInfo("RoleName", "ROLE_CODE", true);
        RoleExtendInfo extendInfo = new RoleExtendInfo("Description", true);

        SysRoleDomain domain = factory.newInstance(null, basicInfo, extendInfo, 1L);

        assertNotNull(domain);
        assertNull(domain.getRoleId());
    }
}
