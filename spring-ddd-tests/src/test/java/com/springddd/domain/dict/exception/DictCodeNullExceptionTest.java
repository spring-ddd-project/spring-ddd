package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictCodeNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        DictCodeNullException exception = new DictCodeNullException();
        assertNotNull(exception);
        assertEquals("Dict code cannot be null", exception.getMessage());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(DictCodeNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(DictCodeNullException.class));
    }
}
