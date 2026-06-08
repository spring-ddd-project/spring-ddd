package com.springddd.application.service.dept;

import com.springddd.domain.dept.*;
import org.springframework.stereotype.Component;

@Component
public class SysDeptDomainFactoryImpl implements SysDeptDomainFactory {

    @Override
    public SysDeptDomain newInstance(DeptId parentId, DeptBasicInfo basicInfo, DeptExtendInfo extendInfo) {
        SysDeptDomain sysDeptDomain = new SysDeptDomain();

        sysDeptDomain.setParentId(parentId);

        sysDeptDomain.setDeptBasicInfo(basicInfo);
        sysDeptDomain.setDeptExtendInfo(extendInfo);

        sysDeptDomain.setDeleteStatus(false);
        return sysDeptDomain;
    }
}
