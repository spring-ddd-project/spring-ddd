package com.springddd.domain.dept.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeptStatusNullExceptionTest {

    @Test
    void shouldThrowWithDefaultMessage() {
        assertThrows(DeptStatusNullException.class, () -> {
            throw new DeptStatusNullException();
        });
    }

    @Test
    void shouldCreateExceptionInstance() {
        DeptStatusNullException exception = new DeptStatusNullException();
        assertNotNull(exception);
    }

    @Test
    void toString_shouldReturnExceptionInfo() {
        DeptStatusNullException exception = new DeptStatusNullException();
        String str = exception.toString();
        assertTrue(str.contains("DeptStatusNullException"));
    }
}
