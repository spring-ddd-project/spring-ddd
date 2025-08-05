package com.springddd.application.service.user;

import com.springddd.domain.role.RoleId;
import com.springddd.domain.user.SysUserRoleDomain;
import com.springddd.domain.user.SysUserRoleDomainFactory;
import com.springddd.domain.user.UserId;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SysUserRoleDomainFactoryImpl implements SysUserRoleDomainFactory {

    @Override
    public SysUserRoleDomain newInstance(UserId userId, RoleId roleId, Long deptId, String createBy) {
        SysUserRoleDomain sysUserRoleDomain = new SysUserRoleDomain();
        sysUserRoleDomain.setUserId(userId);
        sysUserRoleDomain.setRoleId(roleId);

        LocalDateTime now = LocalDateTime.now();
        sysUserRoleDomain.setDeptId(deptId);
        sysUserRoleDomain.setCreateBy(createBy);
        sysUserRoleDomain.setUpdateBy(createBy);
        sysUserRoleDomain.setCreateTime(now);
        sysUserRoleDomain.setUpdateTime(now);
        sysUserRoleDomain.setDeleteStatus("0");
        return sysUserRoleDomain;
    }
}
