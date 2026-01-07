package com.springddd.domain.role.state;

import com.springddd.domain.role.SysRoleDomain;

public class EnabledRoleState implements RoleState {
    @Override
    public void enable(SysRoleDomain domain) {
        // Already enabled
    }

    @Override
    public void disable(SysRoleDomain domain) {
        domain.getRoleBasicInfo().setRoleStatus(false);
        domain.setState(new DisabledRoleState());
    }
}




















