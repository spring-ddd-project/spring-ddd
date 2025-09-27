package com.springddd.domain;

import com.springddd.domain.dept.exception.DeptIdNullException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DomainExceptionTest {

    @Test
    void shouldGetErrorCodeFromException() {
        DeptIdNullException exception = new DeptIdNullException();
        assertEquals(ErrorCode.DEPT_ID_NULL, exception.getErrorCode());
    }

    @Test
    void shouldGetCodeFromException() {
        DeptIdNullException exception = new DeptIdNullException();
        assertEquals(1301, exception.getCode());
    }

    @Test
    void shouldGetMessageKeyFromException() {
        DeptIdNullException exception = new DeptIdNullException();
        assertEquals("error.dept.id.null", exception.getMessageKey());
    }

    @Test
    void shouldGetMessageFromException() {
        DeptIdNullException exception = new DeptIdNullException();
        assertEquals("error.dept.id.null", exception.getMessage());
    }

    @Test
    void shouldExtendRuntimeException() {
        DeptIdNullException exception = new DeptIdNullException();
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void shouldStoreArgs() {
        DeptIdNullException exception = new DeptIdNullException();
        assertNotNull(exception.getArgs());
    }
}
