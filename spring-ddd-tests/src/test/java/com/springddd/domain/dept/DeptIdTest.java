package com.springddd.domain.dept;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeptIdTest {

    @Test
    void shouldCreateDeptIdWithValue() {
        Long expectedValue = 1L;
        DeptId deptId = new DeptId(expectedValue);
        assertEquals(expectedValue, deptId.value());
    }

    @Test
    void shouldReturnValueFromLongWrapper() {
        Long value = 123L;
        DeptId deptId = new DeptId(value);
        assertEquals(123L, deptId.value());
    }

    @Test
    void shouldHandleNullValue() {
        DeptId deptId = new DeptId(null);
        assertNull(deptId.value());
    }
}
