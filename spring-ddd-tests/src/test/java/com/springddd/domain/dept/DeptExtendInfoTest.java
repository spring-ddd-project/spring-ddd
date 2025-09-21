package com.springddd.domain.dept;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeptExtendInfoTest {

    @Test
    void shouldCreateDeptExtendInfoWithAllFields() {
        Integer sortOrder = 1;
        Boolean deptStatus = true;
        DeptExtendInfo info = new DeptExtendInfo(sortOrder, deptStatus);
        assertEquals(sortOrder, info.sortOrder());
        assertEquals(deptStatus, info.deptStatus());
    }

    @Test
    void shouldCreateDeptExtendInfoWithNullStatus() {
        DeptExtendInfo info = new DeptExtendInfo(5, null);
        assertEquals(5, info.sortOrder());
        assertNull(info.deptStatus());
    }

    @Test
    void shouldCreateDeptExtendInfoWithZeroSortOrder() {
        DeptExtendInfo info = new DeptExtendInfo(0, false);
        assertEquals(0, info.sortOrder());
        assertFalse(info.deptStatus());
    }
}
