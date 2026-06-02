package com.springddd.domain.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorCodeTest {

    @Test
    void shouldReturnDeptStatusNull() {
        ErrorCode code = ErrorCode.DEPT_STATUS_NULL;
        assertNotNull(code);
        assertEquals(1303, code.getCode());
        assertTrue(code.getMessageKey().contains("dept"));
    }

    @Test
    void shouldReturnDeptNameNull() {
        ErrorCode code = ErrorCode.DEPT_NAME_NULL;
        assertNotNull(code);
        assertEquals(1300, code.getCode());
    }

    @Test
    void shouldReturnDeptIdNull() {
        ErrorCode code = ErrorCode.DEPT_ID_NULL;
        assertNotNull(code);
        assertEquals(1301, code.getCode());
    }

    @Test
    void shouldReturnDeptSortOrderNull() {
        ErrorCode code = ErrorCode.DEPT_SORT_ORDER_NULL;
        assertNotNull(code);
        assertEquals(1302, code.getCode());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        String str = ErrorCode.DEPT_STATUS_NULL.toString();
        assertTrue(str.contains("DEPT_STATUS_NULL"));
    }
}
