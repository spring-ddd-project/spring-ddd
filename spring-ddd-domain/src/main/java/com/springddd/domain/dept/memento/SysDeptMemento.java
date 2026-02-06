package com.springddd.domain.dept.memento;

import com.springddd.domain.dept.DeptId;
import com.springddd.domain.dept.DeptBasicInfo;
import com.springddd.domain.dept.DeptExtendInfo;
import lombok.Getter;

@Getter
public class SysDeptMemento {
    private final DeptId parentId;
    private final DeptBasicInfo deptBasicInfo;
    private final DeptExtendInfo deptExtendInfo;

    public SysDeptMemento(DeptId parentId, DeptBasicInfo deptBasicInfo, DeptExtendInfo deptExtendInfo) {
        this.parentId = parentId;
        this.deptBasicInfo = deptBasicInfo;
        this.deptExtendInfo = deptExtendInfo;
    }
}























