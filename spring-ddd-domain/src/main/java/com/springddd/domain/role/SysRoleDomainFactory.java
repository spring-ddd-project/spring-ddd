package com.springddd.domain.role;

import java.util.List;

public interface SysRoleDomainFactory {

    SysRoleDomain newInstance(RoleId roleId,
                              RoleBasicInfo roleBasicInfo, RoleExtendInfo roleExtendInfo,
                              DataPermission dataPermission,
                              Long deptId);
}










































