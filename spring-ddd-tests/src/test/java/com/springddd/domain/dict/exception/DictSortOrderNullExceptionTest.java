package com.springddd.domain.dict.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictSortOrderNullExceptionTest {

    @Test
    void shouldThrowWithDefaultMessage() {
        assertThrows(DictSortOrderNullException.class, () -> {
            throw new DictSortOrderNullException();
        });
    }

    @Test
    void shouldCreateExceptionInstance() {
        DictSortOrderNullException exception = new DictSortOrderNullException();
        assertNotNull(exception);
    }

    @Test
    void toString_shouldReturnExceptionInfo() {
        DictSortOrderNullException exception = new DictSortOrderNullException();
        String str = exception.toString();
        assertTrue(str.contains("DictSortOrderNullException"));
    }
}
