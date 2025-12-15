package com.springddd.domain.role.state;

import com.springddd.domain.role.SysRoleDomain;

public interface RoleState {
    void enable(SysRoleDomain domain);
    void disable(SysRoleDomain domain);
}

















