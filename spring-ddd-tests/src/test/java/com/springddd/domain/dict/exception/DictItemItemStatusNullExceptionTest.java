package com.springddd.domain.dict.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictItemItemStatusNullExceptionTest {

    @Test
    void shouldThrowWithDefaultMessage() {
        assertThrows(DictItemItemStatusNullException.class, () -> {
            throw new DictItemItemStatusNullException();
        });
    }

    @Test
    void shouldCreateExceptionInstance() {
        DictItemItemStatusNullException exception = new DictItemItemStatusNullException();
        assertNotNull(exception);
    }

    @Test
    void toString_shouldReturnExceptionInfo() {
        DictItemItemStatusNullException exception = new DictItemItemStatusNullException();
        String str = exception.toString();
        assertTrue(str.contains("DictItemItemStatusNullException"));
    }
}
