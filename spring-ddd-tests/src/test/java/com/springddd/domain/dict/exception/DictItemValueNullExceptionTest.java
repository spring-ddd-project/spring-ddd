package com.springddd.domain.dict.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictItemValueNullExceptionTest {

    @Test
    void shouldThrowWithDefaultMessage() {
        assertThrows(DictItemValueNullException.class, () -> {
            throw new DictItemValueNullException();
        });
    }

    @Test
    void shouldCreateExceptionInstance() {
        DictItemValueNullException exception = new DictItemValueNullException();
        assertNotNull(exception);
    }

    @Test
    void toString_shouldReturnExceptionInfo() {
        DictItemValueNullException exception = new DictItemValueNullException();
        String str = exception.toString();
        assertTrue(str.contains("DictItemValueNullException"));
    }
}
