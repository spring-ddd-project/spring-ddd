package com.springddd.domain.user.exception;

import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsernameExceptionTest {

    @Test
    void constructor_ShouldSetCorrectErrorCode() {
        // When
        UsernameException exception = new UsernameException();

        // Then
        assertEquals(ErrorCode.USER_NAME_NULL, exception.getErrorCode());
        assertEquals(1000, exception.getCode());
        assertEquals("error.user.name.null", exception.getMessageKey());
    }

    @Test
    void getMessage_ShouldReturnCorrectMessage() {
        // When
        UsernameException exception = new UsernameException();

        // Then
        assertEquals("error.user.name.null", exception.getMessage());
    }
}
