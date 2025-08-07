package com.springddd.application.service.role;

import com.springddd.domain.role.*;
import org.springframework.stereotype.Component;

@Component
public class SysRoleDomainFactoryImpl implements SysRoleDomainFactory {

    @Override
    public SysRoleDomain newInstance(RoleId roleId, RoleBasicInfo roleBasicInfo, RoleExtendInfo roleExtendInfo, Long deptId) {
        SysRoleDomain sysRoleDomain = new SysRoleDomain();
        sysRoleDomain.setRoleId(roleId);
        sysRoleDomain.setRoleBasicInfo(roleBasicInfo);
        sysRoleDomain.setRoleExtendInfo(roleExtendInfo);

        sysRoleDomain.setDeptId(deptId);
        sysRoleDomain.setDeleteStatus("0");
        return sysRoleDomain;
    }
}
