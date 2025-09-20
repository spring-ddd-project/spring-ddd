package com.springddd.domain.role.exception;

import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleCodeNullExceptionTest {

    @Test
    void constructor_ShouldSetCorrectErrorCode() {
        // When
        RoleCodeNullException exception = new RoleCodeNullException();

        // Then
        assertEquals(ErrorCode.ROLE_CODE_NULL, exception.getErrorCode());
        assertEquals(1100, exception.getCode());
        assertEquals("error.role.code.null", exception.getMessageKey());
    }

    @Test
    void getMessage_ShouldReturnCorrectMessage() {
        // When
        RoleCodeNullException exception = new RoleCodeNullException();

        // Then
        assertEquals("error.role.code.null", exception.getMessage());
    }
}
