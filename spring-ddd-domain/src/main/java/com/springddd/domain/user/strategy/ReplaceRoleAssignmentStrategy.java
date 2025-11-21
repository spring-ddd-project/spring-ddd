package com.springddd.domain.user.strategy;

import com.springddd.domain.user.SysUserRoleDomain;
import java.util.List;
import java.util.stream.Collectors;

public class ReplaceRoleAssignmentStrategy implements RoleAssignmentStrategy {
    @Override
    public List<SysUserRoleDomain> assign(Long userId, List<Long> roleIds) {
        return roleIds.stream().map(roleId -> {
            SysUserRoleDomain domain = new SysUserRoleDomain();
            domain.setUserId(userId);
            domain.setRoleId(roleId);
            return domain;
        }).collect(Collectors.toList());
    }
}
