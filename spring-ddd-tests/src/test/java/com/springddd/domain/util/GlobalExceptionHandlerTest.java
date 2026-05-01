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
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private MessageSource messageSource;

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler(messageSource);
    }

    @Test
    void shouldHandleDomainException() {
        DomainException exception = new DomainException(ErrorCode.USER_NAME_NULL) {};
        when(messageSource.getMessage(eq("error.user.name.null"), any(), any(Locale.class)))
                .thenReturn("Username cannot be null");

        Mono<ApiResponse> result = globalExceptionHandler.handleDomainException(exception, Locale.getDefault());

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(1000, response.getCode());
                    assertEquals("Username cannot be null", response.getMessage());
                })
                .verifyComplete();
    }

    @Test
    void shouldHandleDomainExceptionWithArgs() {
        DomainException exception = new DomainException(ErrorCode.USER_NAME_NULL, "testUser") {};
        when(messageSource.getMessage(eq("error.user.name.null"), eq(new Object[]{"testUser"}), any(Locale.class)))
                .thenReturn("Username testUser cannot be null");

        Mono<ApiResponse> result = globalExceptionHandler.handleDomainException(exception, Locale.getDefault());

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(1000, response.getCode());
                    assertEquals("Username testUser cannot be null", response.getMessage());
                })
                .verifyComplete();
    }

    @Test
    void shouldHandleWebExchangeBindException() throws Exception {
        // Create WebExchangeBindException using a simpler approach with a mock MethodParameter
        org.springframework.core.MethodParameter methodParameter = new org.springframework.core.MethodParameter(
                GlobalExceptionHandlerTest.class.getDeclaredMethod("dummyMethod", String.class), 0);

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "object");
        bindingResult.addError(new FieldError("object", "fieldName", "Field cannot be null"));

        WebExchangeBindException exception = new WebExchangeBindException(methodParameter, bindingResult);

        Mono<ApiResponse> result = globalExceptionHandler.handleValidationException(exception);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(501, response.getCode());
                    assertTrue(response.getMessage().contains("fieldName"));
                })
                .verifyComplete();
    }

    @Test
    void shouldHandleBadCredentialsException() {
        BadCredentialsException exception = new BadCredentialsException("Invalid credentials");

        Mono<ApiResponse> result = globalExceptionHandler.handleGenericException(exception);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(302, response.getCode());
                    assertEquals("Invalid credentials", response.getMessage());
                })
                .verifyComplete();
    }

    @Test
    void shouldHandleGenericException() {
        Exception exception = new RuntimeException("Something went wrong");

        Mono<ApiResponse> result = globalExceptionHandler.handleGenericException(exception);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(500, response.getCode());
                    assertTrue(response.getMessage().contains("Internal Server Error"));
                    assertTrue(response.getMessage().contains("Something went wrong"));
                })
                .verifyComplete();
    }

    @Test
    void shouldHandleAnotherDomainException() {
        DomainException exception = new DomainException(ErrorCode.ROLE_NAME_NULL) {};
        when(messageSource.getMessage(eq("error.role.name.null"), any(), any(Locale.class)))
                .thenReturn("Role name cannot be null");

        Mono<ApiResponse> result = globalExceptionHandler.handleDomainException(exception, Locale.getDefault());

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(1101, response.getCode());
                    assertEquals("Role name cannot be null", response.getMessage());
                })
                .verifyComplete();
    }

    @Test
    void shouldHandleMenuPermissionDeniedException() {
        DomainException exception = new DomainException(ErrorCode.MENU_PERMISSION_DENIED) {};
        when(messageSource.getMessage(eq("error.menu.permission.denied"), any(), any(Locale.class)))
                .thenReturn("Menu permission denied");

        Mono<ApiResponse> result = globalExceptionHandler.handleDomainException(exception, Locale.getDefault());

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(1207, response.getCode());
                    assertEquals("Menu permission denied", response.getMessage());
                })
                .verifyComplete();
    }

    private void dummyMethod(String field) {
    }
}
