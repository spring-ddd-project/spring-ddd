package com.springddd.domain.dept.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeptIdNullExceptionTest {

    @Test
    void shouldCreateExceptionWithCorrectErrorCode() {
        DeptIdNullException exception = new DeptIdNullException();
        assertEquals(ErrorCode.DEPT_ID_NULL, exception.getErrorCode());
    }

    @Test
    void shouldHaveCorrectCode() {
        DeptIdNullException exception = new DeptIdNullException();
        assertEquals(1301, exception.getCode());
    }

    @Test
    void shouldBeInstanceOfDomainException() {
        DeptIdNullException exception = new DeptIdNullException();
        assertInstanceOf(DomainException.class, exception);
    }

    @Test
    void shouldHaveCorrectMessageKey() {
        DeptIdNullException exception = new DeptIdNullException();
        assertEquals("error.dept.id.null", exception.getMessageKey());
    }
}
