package com.springddd.application.service.dept;

import com.springddd.domain.dept.DeptBasicInfo;
import com.springddd.domain.dept.DeptExtendInfo;
import com.springddd.domain.dept.DeptId;
import com.springddd.domain.dept.SysDeptDomain;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysDeptDomainFactoryImplTest {

    @Test
    void shouldCreateNewInstance() {
        SysDeptDomainFactoryImpl factory = new SysDeptDomainFactoryImpl();

        DeptId parentId = new DeptId(0L);
        DeptBasicInfo basicInfo = new DeptBasicInfo("DeptName");
        DeptExtendInfo extendInfo = new DeptExtendInfo(1, true);

        SysDeptDomain domain = factory.newInstance(parentId, basicInfo, extendInfo);

        assertNotNull(domain);
        assertEquals(parentId, domain.getParentId());
        assertEquals(basicInfo, domain.getDeptBasicInfo());
        assertEquals(extendInfo, domain.getDeptExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldCreateDomainWithNullParentId() {
        SysDeptDomainFactoryImpl factory = new SysDeptDomainFactoryImpl();

        DeptBasicInfo basicInfo = new DeptBasicInfo("DeptName");
        DeptExtendInfo extendInfo = new DeptExtendInfo(1, true);

        SysDeptDomain domain = factory.newInstance(null, basicInfo, extendInfo);

        assertNotNull(domain);
        assertNull(domain.getParentId());
    }
}
