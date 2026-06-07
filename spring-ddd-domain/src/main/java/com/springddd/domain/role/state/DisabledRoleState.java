package com.springddd.domain.role.state;

import com.springddd.domain.role.RoleBasicInfo;
import com.springddd.domain.role.SysRoleDomain;

public class DisabledRoleState implements RoleState {
    @Override
    public void enable(SysRoleDomain domain) {
        RoleBasicInfo old = domain.getRoleBasicInfo();
        domain.setRoleBasicInfo(new RoleBasicInfo(old.roleName(), old.roleCode(), old.roleSort(), true, old.roleOwner()));
        domain.setState(new EnabledRoleState());
    }

    @Override
    public void disable(SysRoleDomain domain) {
        // Already disabled
    }
}



















































