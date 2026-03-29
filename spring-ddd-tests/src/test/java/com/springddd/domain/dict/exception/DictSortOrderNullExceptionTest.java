package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictSortOrderNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        DictSortOrderNullException exception = new DictSortOrderNullException();
        assertNotNull(exception);
        assertEquals("Dict sort order cannot be null", exception.getMessage());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(DictSortOrderNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(DictSortOrderNullException.class));
    }
}
