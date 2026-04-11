package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictNameNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        DictNameNullException exception = new DictNameNullException();
        assertNotNull(exception);
        assertEquals("error.dict.name.null", exception.getMessage());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(DictNameNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(DictNameNullException.class));
    }
}