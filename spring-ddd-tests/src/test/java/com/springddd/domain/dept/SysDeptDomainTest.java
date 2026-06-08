package com.springddd.domain.dept;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysDeptDomainTest {

    @Test
    void shouldCreateSysDeptDomain() {
        SysDeptDomain domain = new SysDeptDomain();
        assertNotNull(domain);
    }

    @Test
    void shouldSetAndGetId() {
        SysDeptDomain domain = new SysDeptDomain();
        DeptId id = new DeptId(1L);
        domain.setId(id);
        assertEquals(id, domain.getId());
    }

    @Test
    void shouldSetAndGetParentId() {
        SysDeptDomain domain = new SysDeptDomain();
        DeptId parentId = new DeptId(0L);
        domain.setParentId(parentId);
        assertEquals(parentId, domain.getParentId());
    }

    @Test
    void shouldSetAndGetBasicInfo() {
        SysDeptDomain domain = new SysDeptDomain();
        DeptBasicInfo basicInfo = new DeptBasicInfo("Test Dept");
        domain.setDeptBasicInfo(basicInfo);
        assertEquals(basicInfo, domain.getDeptBasicInfo());
    }

    @Test
    void shouldSetAndGetExtendInfo() {
        SysDeptDomain domain = new SysDeptDomain();
        DeptExtendInfo extendInfo = new DeptExtendInfo(1, true);
        domain.setDeptExtendInfo(extendInfo);
        assertEquals(extendInfo, domain.getDeptExtendInfo());
    }

    @Test
    void shouldCallCreate() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.create();
        assertNotNull(domain);
    }

    @Test
    void shouldSetDeleteStatusOnDelete() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setDeleteStatus(false);
        domain.delete();
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void shouldClearDeleteStatusOnRestore() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setDeleteStatus(true);
        domain.restore();
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldSetAndGetDeleteStatus() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setDeleteStatus(true);
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void shouldSetAndGetCreateBy() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setCreateBy("admin");
        assertEquals("admin", domain.getCreateBy());
    }

    @Test
    void shouldSetAndGetUpdateBy() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setUpdateBy("admin");
        assertEquals("admin", domain.getUpdateBy());
    }

    @Test
    void shouldSetAndGetVersion() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setVersion(1);
        assertEquals(1, domain.getVersion());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        SysDeptDomain domain = new SysDeptDomain();
        String str = domain.toString();
        assertTrue(str.contains("SysDeptDomain"));
    }
}
