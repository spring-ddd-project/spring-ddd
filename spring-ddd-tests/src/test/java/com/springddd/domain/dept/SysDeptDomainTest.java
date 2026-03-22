package com.springddd.domain.dept;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysDeptDomainTest {

    @Test
    void shouldCreateSysDeptDomainInstance() {
        SysDeptDomain domain = new SysDeptDomain();
        assertNotNull(domain);
    }

    @Test
    void shouldSetIdOnCreate() {
        SysDeptDomain domain = new SysDeptDomain();
        DeptId deptId = new DeptId(1L);
        domain.setId(deptId);
        assertEquals(deptId, domain.getId());
    }

    @Test
    void shouldSetParentIdOnCreate() {
        SysDeptDomain domain = new SysDeptDomain();
        DeptId parentId = new DeptId(0L);
        domain.setParentId(parentId);
        assertEquals(parentId, domain.getParentId());
    }

    @Test
    void shouldSetDeptBasicInfo() {
        SysDeptDomain domain = new SysDeptDomain();
        DeptBasicInfo basicInfo = new DeptBasicInfo("Engineering");
        domain.setDeptBasicInfo(basicInfo);
        assertEquals(basicInfo, domain.getDeptBasicInfo());
    }

    @Test
    void shouldSetDeptExtendInfo() {
        SysDeptDomain domain = new SysDeptDomain();
        DeptExtendInfo extendInfo = new DeptExtendInfo(1, true);
        domain.setDeptExtendInfo(extendInfo);
        assertEquals(extendInfo, domain.getDeptExtendInfo());
    }

    @Test
    void shouldCallCreateMethod() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.create();
        assertNotNull(domain);
    }

    @Test
    void shouldUpdateAllFieldsInUpdateMethod() {
        SysDeptDomain domain = new SysDeptDomain();
        DeptId parentId = new DeptId(1L);
        DeptBasicInfo basicInfo = new DeptBasicInfo("Engineering");
        DeptExtendInfo extendInfo = new DeptExtendInfo(1, true);

        domain.update(parentId, basicInfo, extendInfo);

        assertEquals(parentId, domain.getParentId());
        assertEquals(basicInfo, domain.getDeptBasicInfo());
        assertEquals(extendInfo, domain.getDeptExtendInfo());
    }

    @Test
    void shouldSetDeleteStatusToTrueOnDelete() {
        SysDeptDomain domain = new SysDeptDomain();
        assertNull(domain.getDeleteStatus());
        domain.delete();
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void shouldSetDeleteStatusToFalseOnRestore() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.delete();
        assertTrue(domain.getDeleteStatus());
        domain.restore();
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldAllowUpdateWithNullParentId() {
        SysDeptDomain domain = new SysDeptDomain();
        DeptBasicInfo basicInfo = new DeptBasicInfo("Engineering");
        DeptExtendInfo extendInfo = new DeptExtendInfo(1, true);

        domain.update(null, basicInfo, extendInfo);

        assertNull(domain.getParentId());
        assertEquals(basicInfo, domain.getDeptBasicInfo());
        assertEquals(extendInfo, domain.getDeptExtendInfo());
    }

    @Test
    void shouldAllowUpdateWithNullExtendInfo() {
        SysDeptDomain domain = new SysDeptDomain();
        DeptId parentId = new DeptId(1L);
        DeptBasicInfo basicInfo = new DeptBasicInfo("Engineering");

        domain.update(parentId, basicInfo, null);

        assertEquals(parentId, domain.getParentId());
        assertEquals(basicInfo, domain.getDeptBasicInfo());
        assertNull(domain.getDeptExtendInfo());
    }
}
