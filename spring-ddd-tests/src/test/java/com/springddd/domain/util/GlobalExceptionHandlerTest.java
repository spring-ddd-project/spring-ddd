package com.springddd.domain.util;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.core.MethodParameter;
import org.springframework.web.server.ServerWebInputException;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private MessageSource messageSource;

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler(messageSource);
    }

    @Test
    void testHandleDomainException() {
        when(messageSource.getMessage(eq("error.user.name.null"), any(), eq(Locale.getDefault())))
                .thenReturn("User name cannot be null");

        TestDomainException exception = new TestDomainException(ErrorCode.USER_NAME_NULL);
        ApiResponse response = handler.handleDomainException(exception, Locale.getDefault()).block();

        assertNotNull(response);
        assertEquals(1000, response.getCode());
        assertEquals("User name cannot be null", response.getMessage());
    }

    @Test
    void testHandleDomainExceptionWithArgs() {
        when(messageSource.getMessage(eq("error.role.name.null"), eq(new Object[]{"admin", "role"}), eq(Locale.getDefault())))
                .thenReturn("Role name 'admin' is invalid for type 'role'");

        TestDomainException exception = new TestDomainException(ErrorCode.ROLE_NAME_NULL, "admin", "role");
        ApiResponse response = handler.handleDomainException(exception, Locale.getDefault()).block();

        assertNotNull(response);
        assertEquals(1101, response.getCode());
        assertEquals("Role name 'admin' is invalid for type 'role'", response.getMessage());
    }

    @Test
    void testHandleDomainExceptionDifferentErrorCodes() {
        // Test various error codes directly on the exception
        assertEquals(1000, new TestDomainException(ErrorCode.USER_NAME_NULL).getCode());
        assertEquals(1100, new TestDomainException(ErrorCode.ROLE_CODE_NULL).getCode());
        assertEquals(1200, new TestDomainException(ErrorCode.MENU_NAME_NULL).getCode());
        assertEquals(1300, new TestDomainException(ErrorCode.DEPT_NAME_NULL).getCode());
        assertEquals(1400, new TestDomainException(ErrorCode.DICT_NAME_NULL).getCode());
        assertEquals(1500, new TestDomainException(ErrorCode.GEN_INFO_PACKAGE_NAME_NULL).getCode());
    }

    @Test
    void testHandleValidationException() {
        WebExchangeBindException exception = mock(WebExchangeBindException.class);
        org.springframework.validation.FieldError fieldError1 = new org.springframework.validation.FieldError(
                "user", "name", "Name is required");
        org.springframework.validation.FieldError fieldError2 = new org.springframework.validation.FieldError(
                "user", "email", "Email is invalid");

        when(exception.getFieldErrors()).thenReturn(
                Arrays.asList(fieldError1, fieldError2)
        );

        ApiResponse response = handler.handleValidationException(exception).block();

        assertNotNull(response);
        assertEquals(501, response.getCode());
        assertTrue(response.getMessage().contains("name: Name is required"));
        assertTrue(response.getMessage().contains("email: Email is invalid"));
    }

    @Test
    void testHandleValidationExceptionWithSingleError() {
        WebExchangeBindException exception = mock(WebExchangeBindException.class);
        org.springframework.validation.FieldError fieldError = new org.springframework.validation.FieldError(
                "user", "password", "Password is too short");

        when(exception.getFieldErrors()).thenReturn(
                List.of(fieldError)
        );

        ApiResponse response = handler.handleValidationException(exception).block();

        assertNotNull(response);
        assertEquals(501, response.getCode());
        assertEquals("password: Password is too short", response.getMessage());
    }

    @Test
    void testHandleGenericException() {
        Exception exception = new RuntimeException("Something went wrong");
        ApiResponse response = handler.handleGenericException(exception).block();

        assertNotNull(response);
        assertEquals(500, response.getCode());
        assertTrue(response.getMessage().contains("Something went wrong"));
    }

    @Test
    void testHandleGenericExceptionWithBadCredentials() {
        BadCredentialsException exception = new BadCredentialsException("Invalid credentials");
        ApiResponse response = handler.handleGenericException(exception).block();

        assertNotNull(response);
        assertEquals(302, response.getCode());
        assertEquals("Invalid credentials", response.getMessage());
    }

    @Test
    void testHandleGenericExceptionWithDifferentExceptionTypes() {
        // NullPointerException
        Exception npe = new NullPointerException("null pointer");
        ApiResponse responseNpe = handler.handleGenericException(npe).block();
        assertNotNull(responseNpe);
        assertEquals(500, responseNpe.getCode());

        // IllegalArgumentException
        Exception iae = new IllegalArgumentException("bad argument");
        ApiResponse responseIae = handler.handleGenericException(iae).block();
        assertNotNull(responseIae);
        assertEquals(500, responseIae.getCode());
    }

    @Test
    void testHandleDomainExceptionMessageResolution() {
        when(messageSource.getMessage(
                eq(ErrorCode.USER_NAME_NULL.getMessageKey()),
                eq(new Object[]{}),
                eq(Locale.CHINA)
        )).thenReturn("用户名不能为空");

        TestDomainException exception = new TestDomainException(ErrorCode.USER_NAME_NULL);
        ApiResponse response = handler.handleDomainException(exception, Locale.CHINA).block();

        assertNotNull(response);
        assertEquals(1000, response.getCode());
        assertEquals("用户名不能为空", response.getMessage());
    }

    // Test helper class to allow testing abstract DomainException
    private static class TestDomainException extends DomainException {
        public TestDomainException(ErrorCode errorCode, Object... args) {
            super(errorCode, args);
        }
    }
}
