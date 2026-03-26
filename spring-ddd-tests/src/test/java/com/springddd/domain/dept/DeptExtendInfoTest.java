package com.springddd.domain.dept;

import com.springddd.domain.dept.exception.DeptStatusNullException;
import com.springddd.domain.dept.exception.SortOrderNullException;
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
    void shouldCreateDeptExtendInfoWithZeroSortOrder() {
        DeptExtendInfo info = new DeptExtendInfo(0, false);
        assertEquals(0, info.sortOrder());
        assertFalse(info.deptStatus());
    }

    @Test
    void shouldThrowExceptionWhenSortOrderIsNull() {
        assertThrows(SortOrderNullException.class, () -> {
            new DeptExtendInfo(null, true);
        });
    }

    @Test
    void shouldThrowExceptionWhenDeptStatusIsNull() {
        assertThrows(DeptStatusNullException.class, () -> {
            new DeptExtendInfo(1, null);
        });
    }

    @Test
    void shouldThrowExceptionWhenBothAreNull() {
        assertThrows(SortOrderNullException.class, () -> {
            new DeptExtendInfo(null, null);
        });
    }
}
