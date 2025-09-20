package com.springddd.domain.role.exception;

import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleNameNullExceptionTest {

    @Test
    void constructor_ShouldSetCorrectErrorCode() {
        // When
        RoleNameNullException exception = new RoleNameNullException();

        // Then
        assertEquals(ErrorCode.ROLE_NAME_NULL, exception.getErrorCode());
        assertEquals(1101, exception.getCode());
        assertEquals("error.role.name.null", exception.getMessageKey());
    }

    @Test
    void getMessage_ShouldReturnCorrectMessage() {
        // When
        RoleNameNullException exception = new RoleNameNullException();

        // Then
        assertEquals("error.role.name.null", exception.getMessage());
    }
}
