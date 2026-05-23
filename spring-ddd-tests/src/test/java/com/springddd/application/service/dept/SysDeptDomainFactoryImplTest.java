package com.springddd.application.service.dept;

import com.springddd.domain.dept.DeptBasicInfo;
import com.springddd.domain.dept.DeptExtendInfo;
import com.springddd.domain.dept.DeptId;
import com.springddd.domain.dept.SysDeptDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysDeptDomainFactoryImplTest {

    private final SysDeptDomainFactoryImpl factory = new SysDeptDomainFactoryImpl();

    @Test
    @DisplayName("should create SysDeptDomain with correct fields set")
    void newInstance() {
        DeptId parentId = new DeptId(1L);
        DeptBasicInfo basicInfo = new DeptBasicInfo("Test Dept");
        DeptExtendInfo extendInfo = new DeptExtendInfo(1, true);

        SysDeptDomain domain = factory.newInstance(parentId, basicInfo, extendInfo);

        assertNotNull(domain);
        assertEquals(parentId, domain.getParentId());
        assertEquals(basicInfo, domain.getDeptBasicInfo());
        assertEquals(extendInfo, domain.getDeptExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }
}
