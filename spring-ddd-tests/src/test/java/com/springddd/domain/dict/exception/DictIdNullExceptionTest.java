package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictIdNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        DictIdNullException exception = new DictIdNullException();
        assertNotNull(exception);
        assertEquals("Dict id cannot be null", exception.getMessage());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(DictIdNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(DictIdNullException.class));
    }
}
