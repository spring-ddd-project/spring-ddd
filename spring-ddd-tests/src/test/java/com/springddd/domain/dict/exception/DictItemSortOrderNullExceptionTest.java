package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictItemSortOrderNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        DictItemSortOrderNullException exception = new DictItemSortOrderNullException();
        assertNotNull(exception);
        assertEquals("Dict item sort order cannot be null", exception.getMessage());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(DictItemSortOrderNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(DictItemSortOrderNullException.class));
    }
}
