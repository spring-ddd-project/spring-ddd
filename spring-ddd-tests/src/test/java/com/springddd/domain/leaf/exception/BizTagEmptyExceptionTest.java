package com.springddd.domain.leaf.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BizTagEmptyExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        BizTagEmptyException exception = new BizTagEmptyException();
        assertNotNull(exception);
        assertEquals("error.leaf.bizTag.empty", exception.getMessage());
    }

    @Test
    void shouldExtendLeafAllocException() {
        assertTrue(LeafAllocException.class.isAssignableFrom(BizTagEmptyException.class));
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(BizTagEmptyException.class));
    }

    @Test
    void shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(BizTagEmptyException.class));
    }

    @Test
    void shouldHaveCorrectErrorCode() {
        BizTagEmptyException exception = new BizTagEmptyException();
        assertEquals(1601, exception.getCode());
    }
}
