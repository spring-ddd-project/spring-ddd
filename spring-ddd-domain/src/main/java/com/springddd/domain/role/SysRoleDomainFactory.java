package com.springddd.domain.role;

public interface SysRoleDomainFactory {

    SysRoleDomain newInstance(RoleId roleId,
                              RoleBasicInfo roleBasicInfo, RoleExtendInfo roleExtendInfo,
                              Long deptId);
}
