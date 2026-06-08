package com.springddd.application.service.dept;

import com.springddd.domain.dept.DeptBasicInfo;
import com.springddd.domain.dept.DeptExtendInfo;
import com.springddd.domain.dept.DeptId;
import com.springddd.domain.dept.SysDeptDomain;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysDeptDomainFactoryImplTest {

    private final SysDeptDomainFactoryImpl factory = new SysDeptDomainFactoryImpl();

    @Test
    void shouldCreateNewInstance() {
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
        DeptBasicInfo basicInfo = new DeptBasicInfo("DeptName");
        DeptExtendInfo extendInfo = new DeptExtendInfo(1, true);

        SysDeptDomain domain = factory.newInstance(null, basicInfo, extendInfo);

        assertNotNull(domain);
        assertNull(domain.getParentId());
        assertEquals(basicInfo, domain.getDeptBasicInfo());
        assertEquals(extendInfo, domain.getDeptExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldSetDeleteStatusToFalse() {
        DeptId parentId = new DeptId(0L);
        DeptBasicInfo basicInfo = new DeptBasicInfo("TestDept");
        DeptExtendInfo extendInfo = new DeptExtendInfo(1, true);

        SysDeptDomain domain = factory.newInstance(parentId, basicInfo, extendInfo);

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldCreateDomainWithValidBasicInfo() {
        DeptId parentId = new DeptId(1L);
        DeptBasicInfo basicInfo = new DeptBasicInfo("Engineering");
        DeptExtendInfo extendInfo = new DeptExtendInfo(10, true);

        SysDeptDomain domain = factory.newInstance(parentId, basicInfo, extendInfo);

        assertNotNull(domain);
        assertEquals("Engineering", domain.getDeptBasicInfo().deptName());
        assertEquals(10, domain.getDeptExtendInfo().sortOrder());
        assertTrue(domain.getDeptExtendInfo().deptStatus());
    }

    @Test
    void shouldCreateSeparateInstances() {
        DeptId parentId1 = new DeptId(0L);
        DeptBasicInfo basicInfo1 = new DeptBasicInfo("Dept1");
        DeptExtendInfo extendInfo1 = new DeptExtendInfo(1, true);

        DeptId parentId2 = new DeptId(1L);
        DeptBasicInfo basicInfo2 = new DeptBasicInfo("Dept2");
        DeptExtendInfo extendInfo2 = new DeptExtendInfo(2, false);

        SysDeptDomain domain1 = factory.newInstance(parentId1, basicInfo1, extendInfo1);
        SysDeptDomain domain2 = factory.newInstance(parentId2, basicInfo2, extendInfo2);

        assertNotSame(domain1, domain2);
        assertNotEquals(domain1.getDeptBasicInfo(), domain2.getDeptBasicInfo());
    }

    @Test
    void shouldCreateDomainWithRootParentId() {
        DeptId rootParentId = new DeptId(0L);
        DeptBasicInfo basicInfo = new DeptBasicInfo("RootChild");
        DeptExtendInfo extendInfo = new DeptExtendInfo(1, true);

        SysDeptDomain domain = factory.newInstance(rootParentId, basicInfo, extendInfo);

        assertNotNull(domain);
        assertEquals(rootParentId, domain.getParentId());
        assertEquals(basicInfo, domain.getDeptBasicInfo());
        assertFalse(domain.getDeleteStatus());
    }
}
