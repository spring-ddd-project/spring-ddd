package com.springddd.domain.role.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleDataScopeNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        RoleDataScopeNullException exception = new RoleDataScopeNullException();
        assertNotNull(exception);
        assertEquals("error.role.dataScope.null", exception.getMessage());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(RoleDataScopeNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(RoleDataScopeNullException.class));
    }

    @Test
    void shouldHaveCorrectErrorCode() {
        RoleDataScopeNullException exception = new RoleDataScopeNullException();
        assertEquals(1102, exception.getCode());
        assertEquals("error.role.dataScope.null", exception.getMessageKey());
    }
}
