package com.springddd.domain.dict.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictDictStatusNullExceptionTest {

    @Test
    void shouldThrowWithDefaultMessage() {
        assertThrows(DictDictStatusNullException.class, () -> {
            throw new DictDictStatusNullException();
        });
    }

    @Test
    void shouldCreateExceptionInstance() {
        DictDictStatusNullException exception = new DictDictStatusNullException();
        assertNotNull(exception);
    }

    @Test
    void toString_shouldReturnExceptionInfo() {
        DictDictStatusNullException exception = new DictDictStatusNullException();
        String str = exception.toString();
        assertTrue(str.contains("DictDictStatusNullException"));
    }
}
