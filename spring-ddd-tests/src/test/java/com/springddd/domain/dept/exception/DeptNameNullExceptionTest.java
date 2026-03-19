package com.springddd.domain.dept.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeptNameNullExceptionTest {

    @Test
    void shouldCreateExceptionWithCorrectErrorCode() {
        DeptNameNullException exception = new DeptNameNullException();
        assertEquals(ErrorCode.DEPT_NAME_NULL, exception.getErrorCode());
    }

    @Test
    void shouldHaveCorrectCode() {
        DeptNameNullException exception = new DeptNameNullException();
        assertEquals(1300, exception.getCode());
    }

    @Test
    void shouldBeInstanceOfDomainException() {
        DeptNameNullException exception = new DeptNameNullException();
        assertInstanceOf(DomainException.class, exception);
    }

    @Test
    void shouldHaveCorrectMessageKey() {
        DeptNameNullException exception = new DeptNameNullException();
        assertEquals("error.dept.name.null", exception.getMessageKey());
    }
}
