package com.springddd.domain.user.strategy;

import com.springddd.domain.user.SysUserRoleDomain;
import java.util.List;

public interface RoleAssignmentStrategy {
    List<SysUserRoleDomain> assign(Long userId, List<Long> roleIds);
}




