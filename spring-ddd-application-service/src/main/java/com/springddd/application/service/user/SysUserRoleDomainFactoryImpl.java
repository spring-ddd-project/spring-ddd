package com.springddd.application.service.user;

import com.springddd.domain.role.RoleId;
import com.springddd.domain.user.SysUserRoleDomain;
import com.springddd.domain.user.SysUserRoleDomainFactory;
import com.springddd.domain.user.UserId;
import org.springframework.stereotype.Component;

@Component
public class SysUserRoleDomainFactoryImpl implements SysUserRoleDomainFactory {

    @Override
    public SysUserRoleDomain newInstance(UserId userId, RoleId roleId, Long deptId) {
        SysUserRoleDomain sysUserRoleDomain = new SysUserRoleDomain();
        sysUserRoleDomain.setUserId(userId);
        sysUserRoleDomain.setRoleId(roleId);

        sysUserRoleDomain.setDeptId(deptId);
        sysUserRoleDomain.setDeleteStatus(false);
        return sysUserRoleDomain;
    }
}
