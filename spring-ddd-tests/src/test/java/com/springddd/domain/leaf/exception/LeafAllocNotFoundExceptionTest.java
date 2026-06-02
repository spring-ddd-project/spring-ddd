package com.springddd.domain.leaf.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeafAllocNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        LeafAllocNotFoundException exception = new LeafAllocNotFoundException();
        assertNotNull(exception);
        assertEquals("error.leaf.alloc.notFound", exception.getMessage());
    }

    @Test
    void shouldExtendLeafAllocException() {
        assertTrue(LeafAllocException.class.isAssignableFrom(LeafAllocNotFoundException.class));
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(LeafAllocNotFoundException.class));
    }

    @Test
    void shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(LeafAllocNotFoundException.class));
    }

    @Test
    void shouldHaveCorrectErrorCode() {
        LeafAllocNotFoundException exception = new LeafAllocNotFoundException();
        assertEquals(1605, exception.getCode());
    }
}
