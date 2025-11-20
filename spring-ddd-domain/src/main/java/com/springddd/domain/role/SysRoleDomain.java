package com.springddd.domain.role;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleDomain extends AbstractDomainMask {

    private RoleId roleId;

    private RoleBasicInfo roleBasicInfo;

    private RoleExtendInfo roleExtendInfo;

    private DataPermission dataPermission;

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
