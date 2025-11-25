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

    public void addObserver(com.springddd.domain.dept.observer.DeptObserver observer) {
        observers.add(observer);
    }

    public void create() {}

    public void update(DeptId parentId, DeptBasicInfo basicInfo, DeptExtendInfo extendInfo) {
        this.parentId = parentId;
        this.deptBasicInfo = basicInfo;
        this.deptExtendInfo = extendInfo;
        observers.forEach(o -> o.onUpdate(this));
    }

    public void delete() {
        super.setDeleteStatus(true);
    }

    public void restore() {
        super.setDeleteStatus(false);
    }
}
