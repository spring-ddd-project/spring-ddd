package com.springddd.domain.dept;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDeptDomain extends AbstractDomainMask implements Cloneable {

    private DeptId deptIdentifier;

    private DeptId parentId;

    private DeptBasicInfo deptBasicInfo;

    private DeptExtendInfo deptExtendInfo;

    @Override
    public SysDeptDomain clone() {
        try {
            SysDeptDomain clone = (SysDeptDomain) super.clone();
            if (this.deptIdentifier != null) clone.setDeptIdentifier(new DeptId(this.deptIdentifier.value()));
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

    public com.springddd.domain.dept.memento.SysDeptMemento saveToMemento() {
        return new com.springddd.domain.dept.memento.SysDeptMemento(this.parentId, this.deptBasicInfo, this.deptExtendInfo);
    }

    public void restoreFromMemento(com.springddd.domain.dept.memento.SysDeptMemento memento) {
        this.parentId = memento.getParentId();
        this.deptBasicInfo = memento.getDeptBasicInfo();
        this.deptExtendInfo = memento.getDeptExtendInfo();
    }
}
























































