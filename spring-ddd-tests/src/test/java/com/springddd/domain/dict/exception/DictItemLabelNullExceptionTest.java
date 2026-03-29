package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DictItemLabelNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        DictItemLabelNullException exception = new DictItemLabelNullException();
        assertNotNull(exception);
        assertEquals("Dict item label cannot be null", exception.getMessage());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(DictItemLabelNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(DictItemLabelNullException.class));
    }
}
