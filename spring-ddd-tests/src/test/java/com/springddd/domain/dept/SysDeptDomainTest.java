package com.springddd.domain.dept;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysDeptDomainTest {

    @Test
    void create_shouldInitializeDomain() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setId(new DeptId(1L));
        domain.setDeptBasicInfo(new DeptBasicInfo("部门A"));
        domain.create();
        assertNotNull(domain);
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void update_shouldModifyDomain() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setId(new DeptId(1L));

        DeptId newParentId = new DeptId(0L);
        DeptBasicInfo newBasicInfo = new DeptBasicInfo("新部门");
        DeptExtendInfo newExtendInfo = new DeptExtendInfo(1, true);

        domain.update(newParentId, newBasicInfo, newExtendInfo);

        assertEquals(newParentId, domain.getParentId());
        assertEquals(newBasicInfo, domain.getDeptBasicInfo());
        assertEquals(newExtendInfo, domain.getDeptExtendInfo());
    }

    @Test
    void delete_shouldSetDeleteStatus() {
        SysDeptDomain domain = new SysDeptDomain();
        assertFalse(domain.getDeleteStatus());

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void restore_shouldClearDeleteStatus() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.delete();
        assertTrue(domain.getDeleteStatus());

        domain.restore();

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void setId_and_getId_shouldWork() {
        SysDeptDomain domain = new SysDeptDomain();
        DeptId id = new DeptId(100L);
        domain.setId(id);
        assertEquals(id, domain.getId());
    }

    @Test
    void setParentId_and_getParentId_shouldWork() {
        SysDeptDomain domain = new SysDeptDomain();
        DeptId parentId = new DeptId(50L);
        domain.setParentId(parentId);
        assertEquals(parentId, domain.getParentId());
    }

    @Test
    void setDeptBasicInfo_and_getDeptBasicInfo_shouldWork() {
        SysDeptDomain domain = new SysDeptDomain();
        DeptBasicInfo basicInfo = new DeptBasicInfo("测试部门");
        domain.setDeptBasicInfo(basicInfo);
        assertEquals(basicInfo, domain.getDeptBasicInfo());
    }

    @Test
    void setDeptExtendInfo_and_getDeptExtendInfo_shouldWork() {
        SysDeptDomain domain = new SysDeptDomain();
        DeptExtendInfo extendInfo = new DeptExtendInfo(1, true);
        domain.setDeptExtendInfo(extendInfo);
        assertEquals(extendInfo, domain.getDeptExtendInfo());
    }
}
