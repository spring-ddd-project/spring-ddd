package com.springddd.domain.dept;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeptIdTest {

    @Test
    void shouldCreateDeptIdWithValidValue() {
        DeptId deptId = new DeptId(1L);
        assertEquals(1L, deptId.value());
    }

    @Test
    void shouldCreateDeptIdWithZero() {
        DeptId deptId = new DeptId(0L);
        assertEquals(0L, deptId.value());
    }

    @Test
    void equals_shouldWorkForSameValue() {
        DeptId id1 = new DeptId(1L);
        DeptId id2 = new DeptId(1L);
        assertEquals(id1, id2);
    }

    @Test
    void equals_shouldFailForDifferentValue() {
        DeptId id1 = new DeptId(1L);
        DeptId id2 = new DeptId(2L);
        assertNotEquals(id1, id2);
    }

    @Test
    void toString_shouldReturnValueAsString() {
        DeptId deptId = new DeptId(123L);
        assertEquals("DeptId[value=123]", deptId.toString());
    }
}