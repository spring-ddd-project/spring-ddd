package com.springddd.domain.dict.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictItemLabelNullExceptionTest {

    @Test
    void shouldThrowWithDefaultMessage() {
        assertThrows(DictItemLabelNullException.class, () -> {
            throw new DictItemLabelNullException();
        });
    }

    @Test
    void shouldCreateExceptionInstance() {
        DictItemLabelNullException exception = new DictItemLabelNullException();
        assertNotNull(exception);
    }

    @Test
    void toString_shouldReturnExceptionInfo() {
        DictItemLabelNullException exception = new DictItemLabelNullException();
        String str = exception.toString();
        assertTrue(str.contains("DictItemLabelNullException"));
    }
}
