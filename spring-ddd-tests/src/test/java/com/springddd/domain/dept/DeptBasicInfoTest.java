package com.springddd.domain.dept;

import com.springddd.domain.dept.exception.DeptNameNullException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeptBasicInfoTest {

    @Test
    void shouldCreateWithValidName() {
        DeptBasicInfo info = new DeptBasicInfo("部门A");
        assertEquals("部门A", info.deptName());
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThrows(DeptNameNullException.class, () -> new DeptBasicInfo(null));
    }

    @Test
    void shouldThrowWhenNameIsEmpty() {
        assertThrows(DeptNameNullException.class, () -> new DeptBasicInfo(""));
    }

    @Test
    void equals_shouldWorkForSameName() {
        DeptBasicInfo info1 = new DeptBasicInfo("部门A");
        DeptBasicInfo info2 = new DeptBasicInfo("部门A");
        assertEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentName() {
        DeptBasicInfo info1 = new DeptBasicInfo("部门A");
        DeptBasicInfo info2 = new DeptBasicInfo("部门B");
        assertNotEquals(info1, info2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        DeptBasicInfo info1 = new DeptBasicInfo("部门A");
        DeptBasicInfo info2 = new DeptBasicInfo("部门A");
        assertEquals(info1.hashCode(), info2.hashCode());
    }

    @Test
    void toString_shouldReturnName() {
        DeptBasicInfo info = new DeptBasicInfo("测试部门");
        assertEquals("DeptBasicInfo[deptName=测试部门]", info.toString());
    }
}