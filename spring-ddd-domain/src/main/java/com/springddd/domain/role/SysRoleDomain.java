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

    private DataPermission dataPermission;

    private com.springddd.domain.role.state.RoleState state;

    public void setState(com.springddd.domain.role.state.RoleState state) {
        this.state = state;
    }

    public void enable() {
        if (state == null) state = roleBasicInfo.getRoleStatus() ? new com.springddd.domain.role.state.EnabledRoleState() : new com.springddd.domain.role.state.DisabledRoleState();
        state.enable(this);
    }

    public void disable() {
        if (state == null) state = roleBasicInfo.getRoleStatus() ? new com.springddd.domain.role.state.EnabledRoleState() : new com.springddd.domain.role.state.DisabledRoleState();
        state.disable(this);
    }

    @Override
    public SysRoleDomain clone() {
        try {
            SysRoleDomain clone = (SysRoleDomain) super.clone();
            if (this.roleId != null) clone.setRoleId(new RoleId(this.roleId.value()));
            if (this.roleBasicInfo != null) {
                RoleBasicInfo basic = new RoleBasicInfo();
                basic.setRoleName(this.roleBasicInfo.getRoleName());
                basic.setRoleCode(this.roleBasicInfo.getRoleCode());
                basic.setRoleSort(this.roleBasicInfo.getRoleSort());
                basic.setRoleStatus(this.roleBasicInfo.getRoleStatus());
                clone.setRoleBasicInfo(basic);
            }
            if (this.roleExtendInfo != null) {
                RoleExtendInfo ext = new RoleExtendInfo();
                ext.setRoleRemark(this.roleExtendInfo.getRoleRemark());
                ext.setOwnerStatus(this.roleExtendInfo.getOwnerStatus());
                clone.setRoleExtendInfo(ext);
            }
            if (this.dataPermission != null) {
                DataPermission dp = new DataPermission();
                dp.setDataScope(this.dataPermission.getDataScope());
                dp.setDeptIds(this.dataPermission.getDeptIds());
                clone.setDataPermission(dp);
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    private final java.util.List<com.springddd.domain.role.observer.RoleObserver> observers = new java.util.ArrayList<>();

    public void addObserver(com.springddd.domain.role.observer.RoleObserver observer) {
        observers.add(observer);
    }

    public void create() {}

    public void updateRole(RoleBasicInfo roleBasicInfo, RoleExtendInfo roleExtendInfo, DataPermission dataPermission, Long deptId) {
        this.roleBasicInfo = roleBasicInfo;
        this.roleExtendInfo = roleExtendInfo;
        this.dataPermission = dataPermission;
        super.setDeptId(deptId);
        observers.forEach(o -> o.onUpdate(this));
    }

    public void delete() {
        super.setDeleteStatus(true);
    }
    public void restore() {
        super.setDeleteStatus(false);
    }
}





