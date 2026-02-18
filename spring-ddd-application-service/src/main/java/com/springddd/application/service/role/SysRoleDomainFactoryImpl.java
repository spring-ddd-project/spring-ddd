package com.springddd.application.service.role;

import com.springddd.domain.role.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SysRoleDomainFactoryImpl implements SysRoleDomainFactory {

    @Override
    public SysRoleDomain newInstance(RoleId roleId, RoleBasicInfo roleBasicInfo, RoleExtendInfo roleExtendInfo, DataPermission dataPermission, Long deptId) {
        SysRoleDomain sysRoleDomain = new SysRoleDomain();
        sysRoleDomain.setRoleId(roleId);
        sysRoleDomain.setRoleBasicInfo(roleBasicInfo);
        sysRoleDomain.setRoleExtendInfo(roleExtendInfo);
        sysRoleDomain.setDataPermission(dataPermission);

        sysRoleDomain.setDeptId(deptId);
        sysRoleDomain.setDeleteStatus(false);
        return sysRoleDomain;
    }
}





































