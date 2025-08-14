package com.springddd.domain.dept;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDeptDomain extends AbstractDomainMask {

    private DeptId id;

    private DeptId parentId;

    private DeptBasicInfo deptBasicInfo;

    private DeptExtendInfo deptExtendInfo;

    public void create() {}

    public void update(DeptId parentId, DeptBasicInfo basicInfo, DeptExtendInfo extendInfo) {
        this.parentId = parentId;
        this.deptBasicInfo = basicInfo;
        this.deptExtendInfo = extendInfo;
    }

    public void delete() {
        super.setDeleteStatus(true);
    }
}
