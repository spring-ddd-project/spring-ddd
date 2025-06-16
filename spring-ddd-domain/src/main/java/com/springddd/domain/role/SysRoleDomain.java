package com.springddd.domain.role;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleDomain extends AbstractDomainMask {

    private RoleId roleId;

    private RoleBasicInfo roleBasicInfo;

    private RoleExtendInfo roleExtendInfo;

    public void create() {}

    public void updateRole(RoleBasicInfo roleBasicInfo, RoleExtendInfo roleExtendInfo, Long deptId, String updateBy) {
        this.roleBasicInfo = roleBasicInfo;
        this.roleExtendInfo = roleExtendInfo;
        super.setDeptId(deptId);
        super.setUpdateBy(updateBy);
        super.setUpdateTime(LocalDateTime.now());
    }

    public void delete(String updateBy) {
        super.setDeleteStatus("1");
        super.setUpdateBy(updateBy);
        super.setUpdateTime(LocalDateTime.now());
    }
}
