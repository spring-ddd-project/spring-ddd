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

    private final java.util.List<com.springddd.domain.dept.observer.DeptObserver> observers = new java.util.ArrayList<>();

    private com.springddd.domain.dept.state.DeptState state;

    public void setState(com.springddd.domain.dept.state.DeptState state) {
        this.state = state;
    }

    public void addObserver(com.springddd.domain.dept.observer.DeptObserver observer) {
        observers.add(observer);
    }

    public void create() {
        this.state = new com.springddd.domain.dept.state.ActiveDeptState();
    }

    public void update(DeptId parentId, DeptBasicInfo basicInfo, DeptExtendInfo extendInfo) {
        this.parentId = parentId;
        this.deptBasicInfo = basicInfo;
        this.deptExtendInfo = extendInfo;
        observers.forEach(o -> o.onUpdate(this));
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
