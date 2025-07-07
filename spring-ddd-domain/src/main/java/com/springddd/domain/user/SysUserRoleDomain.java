package com.springddd.domain.user;

import com.springddd.domain.AbstractDomainMask;
import com.springddd.domain.role.RoleId;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserRoleDomain extends AbstractDomainMask {

    private UserRoleId userRoleId;

    private UserId userId;

    private RoleId roleId;

    public void create() {}

    public void update(UserId userId, RoleId roleId, Long deptId) {
        this.userId = userId;
        this.roleId = roleId;
        super.setDeptId(deptId);
    }

    public void delete() {
        super.setDeleteStatus(true);
    }
}
