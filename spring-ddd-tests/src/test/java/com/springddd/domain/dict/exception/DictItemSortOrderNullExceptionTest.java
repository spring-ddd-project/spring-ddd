package com.springddd.domain.dict.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictItemSortOrderNullExceptionTest {

    @Test
    void shouldThrowWithDefaultMessage() {
        assertThrows(DictItemSortOrderNullException.class, () -> {
            throw new DictItemSortOrderNullException();
        });
    }

    @Test
    void shouldCreateExceptionInstance() {
        DictItemSortOrderNullException exception = new DictItemSortOrderNullException();
        assertNotNull(exception);
    }

    @Test
    void toString_shouldReturnExceptionInfo() {
        DictItemSortOrderNullException exception = new DictItemSortOrderNullException();
        String str = exception.toString();
        assertTrue(str.contains("DictItemSortOrderNullException"));
    }
}
