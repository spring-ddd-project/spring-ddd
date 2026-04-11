package com.springddd.domain.dept;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeptExtendInfoTest {

    @Test
    void shouldCreateWithValidValues() {
        DeptExtendInfo info = new DeptExtendInfo(1, true);
        assertEquals(1, info.sortOrder());
        assertTrue(info.deptStatus());
    }

    @Test
    void shouldCreateWithZeroValues() {
        DeptExtendInfo info = new DeptExtendInfo(0, false);
        assertEquals(0, info.sortOrder());
        assertFalse(info.deptStatus());
    }

    @Test
    void shouldCreateWithLargeValues() {
        DeptExtendInfo info = new DeptExtendInfo(Integer.MAX_VALUE, true);
        assertEquals(Integer.MAX_VALUE, info.sortOrder());
        assertTrue(info.deptStatus());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        DeptExtendInfo info1 = new DeptExtendInfo(1, true);
        DeptExtendInfo info2 = new DeptExtendInfo(1, true);
        assertEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentSortOrder() {
        DeptExtendInfo info1 = new DeptExtendInfo(1, true);
        DeptExtendInfo info2 = new DeptExtendInfo(2, true);
        assertNotEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentDeptStatus() {
        DeptExtendInfo info1 = new DeptExtendInfo(1, true);
        DeptExtendInfo info2 = new DeptExtendInfo(1, false);
        assertNotEquals(info1, info2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        DeptExtendInfo info1 = new DeptExtendInfo(1, true);
        DeptExtendInfo info2 = new DeptExtendInfo(1, true);
        assertEquals(info1.hashCode(), info2.hashCode());
    }

    @Test
    void toString_shouldReturnValues() {
        DeptExtendInfo info = new DeptExtendInfo(5, true);
        assertTrue(info.toString().contains("5"));
        assertTrue(info.toString().contains("true"));
    }
}
