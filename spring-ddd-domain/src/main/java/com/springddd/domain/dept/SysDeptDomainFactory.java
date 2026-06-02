package com.springddd.domain.dept;

public interface SysDeptDomainFactory {

    SysDeptDomain newInstance(DeptId parentId, DeptBasicInfo basicInfo, DeptExtendInfo extendInfo);
}
