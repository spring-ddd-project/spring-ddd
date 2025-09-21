package com.springddd.domain.role.exception;

import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleDataScopeNullExceptionTest {

    @Test
    void constructor_ShouldSetCorrectErrorCode() {
        // When
        RoleDataScopeNullException exception = new RoleDataScopeNullException();

        // Then
        assertEquals(ErrorCode.ROLE_DATA_SCOPE_NULL, exception.getErrorCode());
        assertEquals(1102, exception.getCode());
        assertEquals("error.role.dataScope.null", exception.getMessageKey());
    }

    @Test
    void getMessage_ShouldReturnCorrectMessage() {
        // When
        RoleDataScopeNullException exception = new RoleDataScopeNullException();

        // Then
        assertEquals("error.role.dataScope.null", exception.getMessage());
    }
}
