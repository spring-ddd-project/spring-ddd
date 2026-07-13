package com.springddd.domain.role;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleMenuDataScopeDomain extends AbstractDomainMask {

    private RoleMenuDataScopeId id;

    private RoleMenuDataScopeInfo roleMenuDataScopeInfo;

    public void create() {}

    public void update(RoleMenuDataScopeInfo info) {
        this.roleMenuDataScopeInfo = info;
    }

    public void delete() {
        super.setDeleteStatus(true);
    }

    public void restore() {
        super.setDeleteStatus(false);
    }
}
