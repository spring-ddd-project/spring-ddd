package com.springddd.domain.leaf.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BizTagNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        BizTagNullException exception = new BizTagNullException();
        assertNotNull(exception);
        assertEquals("error.leaf.bizTag.null", exception.getMessage());
    }

    @Test
    void shouldExtendLeafAllocException() {
        assertTrue(LeafAllocException.class.isAssignableFrom(BizTagNullException.class));
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(BizTagNullException.class));
    }

    @Test
    void shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(BizTagNullException.class));
    }

    @Test
    void shouldHaveCorrectErrorCode() {
        BizTagNullException exception = new BizTagNullException();
        assertEquals(1600, exception.getCode());
    }
}
