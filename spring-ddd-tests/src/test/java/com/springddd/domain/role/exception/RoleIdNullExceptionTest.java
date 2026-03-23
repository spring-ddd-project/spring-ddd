package com.springddd.domain.role.exception;

import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleIdNullExceptionTest {

    @Test
    void constructor_ShouldSetCorrectErrorCode() {
        // When
        RoleIdNullException exception = new RoleIdNullException();

        // Then
        assertEquals(ErrorCode.ROLE_ID_NULL, exception.getErrorCode());
        assertEquals(1103, exception.getCode());
        assertEquals("error.role.id.null", exception.getMessageKey());
    }

    @Test
    void getMessage_ShouldReturnCorrectMessage() {
        // When
        RoleIdNullException exception = new RoleIdNullException();

        // Then
        assertEquals("error.role.id.null", exception.getMessage());
    }
}
