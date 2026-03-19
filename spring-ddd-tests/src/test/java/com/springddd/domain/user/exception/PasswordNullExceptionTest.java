package com.springddd.domain.user.exception;

import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordNullExceptionTest {

    @Test
    void constructor_ShouldSetCorrectErrorCode() {
        // When
        PasswordNullException exception = new PasswordNullException();

        // Then
        assertEquals(ErrorCode.USER_PASSWORD_NULL, exception.getErrorCode());
        assertEquals(1001, exception.getCode());
        assertEquals("error.user.password.null", exception.getMessageKey());
    }

    @Test
    void getMessage_ShouldReturnCorrectMessage() {
        // When
        PasswordNullException exception = new PasswordNullException();

        // Then
        assertEquals("error.user.password.null", exception.getMessage());
    }
}
