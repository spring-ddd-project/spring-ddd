package com.springddd.domain.user;

import com.springddd.domain.role.RoleId;

public interface SysUserRoleDomainFactory {

    SysUserRoleDomain newInstance(UserId userId, RoleId roleId, Long deptId);
}
