package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictItemValueNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        DictItemValueNullException exception = new DictItemValueNullException();
        assertNotNull(exception);
        assertEquals("Dict item value cannot be null", exception.getMessage());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(DictItemValueNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(DictItemValueNullException.class));
    }
}
