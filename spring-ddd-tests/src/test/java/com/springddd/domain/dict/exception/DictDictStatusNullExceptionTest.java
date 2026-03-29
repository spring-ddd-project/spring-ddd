package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictDictStatusNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        DictDictStatusNullException exception = new DictDictStatusNullException();
        assertNotNull(exception);
        assertEquals("Dict status cannot be null", exception.getMessage());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(DictDictStatusNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(DictDictStatusNullException.class));
    }
}
