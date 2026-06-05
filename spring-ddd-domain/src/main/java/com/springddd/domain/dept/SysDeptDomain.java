package com.springddd.domain.dept;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDeptDomain extends AbstractDomainMask implements Cloneable {

    private DeptId id;

    private DeptId parentId;

    private DeptBasicInfo deptBasicInfo;

    private DeptExtendInfo deptExtendInfo;

    @Override
    public SysDeptDomain clone() {
        try {
            SysDeptDomain clone = (SysDeptDomain) doClone();
            if (this.id != null) clone.setId(new DeptId(this.id.value()));
            if (this.parentId != null) clone.setParentId(new DeptId(this.parentId.value()));
            if (this.deptBasicInfo != null) {
                DeptBasicInfo basic = new DeptBasicInfo(this.deptBasicInfo.deptName());
                clone.setDeptBasicInfo(basic);
            }
            if (this.deptExtendInfo != null) {
                DeptExtendInfo ext = new DeptExtendInfo(this.deptExtendInfo.sortOrder(), this.deptExtendInfo.deptStatus());
                clone.setDeptExtendInfo(ext);
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    private com.springddd.domain.dept.state.DeptState state;

    public void setState(com.springddd.domain.dept.state.DeptState state) {
        this.state = state;
    }

    public void create() {
        this.state = new com.springddd.domain.dept.state.ActiveDeptState();
    }

    public void update(DeptId parentId, DeptBasicInfo basicInfo, DeptExtendInfo extendInfo) {
        this.parentId = parentId;
        this.deptBasicInfo = basicInfo;
        this.deptExtendInfo = extendInfo;
    }

    public void delete() {
        if (state == null) state = getDeleteStatus() ? new com.springddd.domain.dept.state.DeletedDeptState() : new com.springddd.domain.dept.state.ActiveDeptState();
        state.delete(this);
    }

    public void restore() {
        if (state == null) state = getDeleteStatus() ? new com.springddd.domain.dept.state.DeletedDeptState() : new com.springddd.domain.dept.state.ActiveDeptState();
        state.restore(this);
    }
}
