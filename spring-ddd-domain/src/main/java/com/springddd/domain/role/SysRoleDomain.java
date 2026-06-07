package com.springddd.domain.role;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleDomain extends AbstractDomainMask implements Cloneable {

    private RoleId roleId;

    private RoleBasicInfo roleBasicInfo;

    private RoleExtendInfo roleExtendInfo;

    @Override
    public SysRoleDomain clone() {
        try {
            SysRoleDomain clone = (SysRoleDomain) doClone();
            if (this.roleId != null) clone.setRoleId(new RoleId(this.roleId.value()));
            if (this.roleBasicInfo != null) {
                RoleBasicInfo basic = new RoleBasicInfo(this.roleBasicInfo.roleName(), this.roleBasicInfo.roleCode(), this.roleBasicInfo.roleSort(), this.roleBasicInfo.roleStatus(), this.roleBasicInfo.roleOwner());
                clone.setRoleBasicInfo(basic);
            }
            if (this.roleExtendInfo != null) {
                RoleExtendInfo ext = new RoleExtendInfo(this.roleExtendInfo.roleRemark(), this.roleExtendInfo.ownerStatus(), this.roleExtendInfo.roleDesc(), this.roleExtendInfo.roleStatus());
                clone.setRoleExtendInfo(ext);
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    private com.springddd.domain.role.state.RoleState state;

    public void setState(com.springddd.domain.role.state.RoleState state) {
        this.state = state;
    }

    public void enable() {
        if (state == null) state = roleBasicInfo.roleStatus() ? new com.springddd.domain.role.state.EnabledRoleState() : new com.springddd.domain.role.state.DisabledRoleState();
        state.enable(this);
    }

    public void disable() {
        if (state == null) state = roleBasicInfo.roleStatus() ? new com.springddd.domain.role.state.EnabledRoleState() : new com.springddd.domain.role.state.DisabledRoleState();
        state.disable(this);
    }

    public void create() {}

    public void updateRole(RoleBasicInfo roleBasicInfo, RoleExtendInfo roleExtendInfo, Long deptId) {
        this.roleBasicInfo = roleBasicInfo;
        this.roleExtendInfo = roleExtendInfo;
        super.setDeptId(deptId);
    }

    public void delete() {
        super.setDeleteStatus(true);
    }
    public void restore() {
        super.setDeleteStatus(false);
    }
}
