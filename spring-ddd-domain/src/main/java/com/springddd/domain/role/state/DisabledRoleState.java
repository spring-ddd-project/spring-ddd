package com.springddd.domain.role.state;

import com.springddd.domain.role.SysRoleDomain;

public class DisabledRoleState implements RoleState {
    @Override
    public void enable(SysRoleDomain domain) {
        domain.getRoleBasicInfo().setRoleStatus(true);
        domain.setState(new EnabledRoleState());
    }

    @Override
    public void disable(SysRoleDomain domain) {
        // Already disabled
    }
}
























