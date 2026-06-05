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

    @Test
    void shouldUpdateSysDeptDomain() {
        SysDeptDomain domain = new SysDeptDomain();
        DeptId parentId = new DeptId(0L);
        DeptBasicInfo basicInfo = new DeptBasicInfo("Updated Dept");
        DeptExtendInfo extendInfo = new DeptExtendInfo(2, false);

        domain.update(parentId, basicInfo, extendInfo);

        assertEquals(parentId, domain.getParentId());
        assertEquals(basicInfo, domain.getDeptBasicInfo());
        assertEquals(extendInfo, domain.getDeptExtendInfo());
    }

    @Test
    void shouldDeleteWhenStateIsNullAndDeleteStatusFalse() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setDeleteStatus(false);

        domain.delete();

        assertTrue(domain.getDeleteStatus());
        assertNotNull(domain.getState());
    }

    @Test
    void shouldDeleteWhenStateIsNullAndDeleteStatusTrue() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setDeleteStatus(true);

        domain.delete();

        assertTrue(domain.getDeleteStatus());
        assertNotNull(domain.getState());
    }

    @Test
    void shouldDeleteWhenStateExists() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setState(new com.springddd.domain.dept.state.ActiveDeptState());

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void shouldRestoreWhenStateIsNullAndDeleteStatusFalse() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setDeleteStatus(false);

        domain.restore();

        assertFalse(domain.getDeleteStatus());
        assertNotNull(domain.getState());
    }

    @Test
    void shouldRestoreWhenStateIsNullAndDeleteStatusTrue() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setDeleteStatus(true);

        domain.restore();

        assertFalse(domain.getDeleteStatus());
        assertNotNull(domain.getState());
    }

    @Test
    void shouldRestoreWhenStateExists() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setState(new com.springddd.domain.dept.state.DeletedDeptState());
        domain.setDeleteStatus(true);

        domain.restore();

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldCloneWithNullFields() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setId(null);
        domain.setParentId(null);
        domain.setDeptBasicInfo(null);
        domain.setDeptExtendInfo(null);

        SysDeptDomain clone = domain.clone();

        assertNotNull(clone);
        assertNull(clone.getId());
        assertNull(clone.getParentId());
        assertNull(clone.getDeptBasicInfo());
        assertNull(clone.getDeptExtendInfo());
    }

    @Test
    void shouldCloneWithAllFields() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setId(new DeptId(1L));
        domain.setParentId(new DeptId(0L));
        domain.setDeptBasicInfo(new DeptBasicInfo("Test Dept"));
        domain.setDeptExtendInfo(new DeptExtendInfo(1, true));

        SysDeptDomain clone = domain.clone();

        assertNotNull(clone);
        assertEquals(1L, clone.getId().value());
        assertEquals(0L, clone.getParentId().value());
        assertEquals("Test Dept", clone.getDeptBasicInfo().deptName());
        assertEquals(1, clone.getDeptExtendInfo().sortOrder());
    }

    @Test
    void shouldHandleCloneNotSupportedException() {
        SysDeptDomain domain = new SysDeptDomain() {
            @Override
            protected Object doClone() throws CloneNotSupportedException {
                throw new CloneNotSupportedException("test");
            }
        };
        assertThrows(AssertionError.class, domain::clone);
    }
}
