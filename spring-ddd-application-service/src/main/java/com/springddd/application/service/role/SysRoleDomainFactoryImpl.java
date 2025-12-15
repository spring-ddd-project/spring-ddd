package com.springddd.application.service.role;

import com.springddd.domain.role.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SysRoleDomainFactoryImpl implements SysRoleDomainFactory {

    @Override
    public SysRoleDomain newInstance(RoleId roleId, RoleBasicInfo roleBasicInfo, RoleExtendInfo roleExtendInfo, Long deptId, String createBy) {
        SysRoleDomain sysRoleDomain = new SysRoleDomain();
        sysRoleDomain.setRoleId(roleId);
        sysRoleDomain.setRoleBasicInfo(roleBasicInfo);
        sysRoleDomain.setRoleExtendInfo(roleExtendInfo);

        LocalDateTime now = LocalDateTime.now();
        sysRoleDomain.setDeptId(deptId);
        sysRoleDomain.setCreateBy(createBy);
        sysRoleDomain.setCreateTime(now);
        sysRoleDomain.setUpdateBy(createBy);
        sysRoleDomain.setUpdateTime(now);
        sysRoleDomain.setDeleteStatus("0");
        return sysRoleDomain;
    }
}
