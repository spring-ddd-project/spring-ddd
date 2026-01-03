package com.springddd.domain.dept;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDeptDomain extends AbstractDomainMask {

    private DeptId deptId;

    private DeptId parentId;

    private DeptBasicInfo deptBasicInfo;

    private DeptExtendInfo deptExtendInfo;
}
