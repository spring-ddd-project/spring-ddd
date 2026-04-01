package com.springddd.domain.dept.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SortOrderNullExceptionTest {

    @Test
    void shouldThrowWithDefaultMessage() {
        assertThrows(SortOrderNullException.class, () -> {
            throw new SortOrderNullException();
        });
    }

    @Test
    void shouldCreateExceptionInstance() {
        SortOrderNullException exception = new SortOrderNullException();
        assertNotNull(exception);
    }

    @Test
    void toString_shouldReturnExceptionInfo() {
        SortOrderNullException exception = new SortOrderNullException();
        String str = exception.toString();
        assertTrue(str.contains("SortOrderNullException"));
    }
}
