package com.springddd.domain.util;

import com.springddd.domain.dept.exception.DeptIdNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.test.StepVerifier;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Test
    @DisplayName("handleDomainException 应返回错误响应")
    void handleDomainException_shouldReturnErrorResponse() {
        DeptIdNullException ex = new DeptIdNullException();
        when(messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), Locale.ENGLISH)).thenReturn("Test error");

        StepVerifier.create(handler.handleDomainException(ex, Locale.ENGLISH))
                .assertNext(response -> {
                    assertThat(response.getCode()).isEqualTo(ex.getCode());
                    assertThat(response.getMessage()).isEqualTo("Test error");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("handleValidationException 应返回验证错误响应")
    void handleValidationException_shouldReturnValidationError() {
        WebExchangeBindException ex = mock(WebExchangeBindException.class);
        when(ex.getFieldErrors()).thenReturn(java.util.List.of());

        StepVerifier.create(handler.handleValidationException(ex))
                .assertNext(response -> assertThat(response.getCode()).isEqualTo(501))
                .verifyComplete();
    }

    @Test
    @DisplayName("handleValidationException 有字段错误时应返回拼接的错误消息")
    void handleValidationException_withFieldErrors_shouldReturnConcatenatedMessage() {
        WebExchangeBindException ex = mock(WebExchangeBindException.class);
        when(ex.getFieldErrors()).thenReturn(java.util.List.of(
                new FieldError("obj", "name", "must not be null"),
                new FieldError("obj", "age", "must be positive")
        ));

        StepVerifier.create(handler.handleValidationException(ex))
                .assertNext(response -> {
                    assertThat(response.getCode()).isEqualTo(501);
                    assertThat(response.getMessage()).contains("name: must not be null");
                    assertThat(response.getMessage()).contains("age: must be positive");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("handleGenericException 当 BadCredentialsException 时应返回 302")
    void handleGenericException_whenBadCredentials_shouldReturn302() {
        BadCredentialsException ex = new BadCredentialsException("Bad creds");

        StepVerifier.create(handler.handleGenericException(ex))
                .assertNext(response -> {
                    assertThat(response.getCode()).isEqualTo(302);
                    assertThat(response.getMessage()).isEqualTo("Bad creds");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("handleGenericException 应返回通用错误响应")
    void handleGenericException_shouldReturnGenericError() {
        Exception ex = new Exception("Something went wrong");

        StepVerifier.create(handler.handleGenericException(ex))
                .assertNext(response -> {
                    assertThat(response.getCode()).isEqualTo(500);
                    assertThat(response.getMessage()).contains("Something went wrong");
                })
                .verifyComplete();
    }
}
