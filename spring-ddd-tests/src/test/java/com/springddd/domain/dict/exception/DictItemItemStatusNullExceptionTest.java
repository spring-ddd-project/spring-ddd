package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictItemItemStatusNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        DictItemItemStatusNullException exception = new DictItemItemStatusNullException();
        assertNotNull(exception);
        assertEquals("Dict item status cannot be null", exception.getMessage());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(DictItemItemStatusNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(DictItemItemStatusNullException.class));
    }
}
