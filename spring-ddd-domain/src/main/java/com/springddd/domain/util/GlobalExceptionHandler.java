package com.springddd.domain.util;

import com.springddd.domain.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.stream.Collectors;

@Component
@RestControllerAdvice
@Order(-1)
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Handle custom business exceptions.
     *
     * @param e      the exception
     * @param locale the locale
     * @return standardized API response
     */
    @ExceptionHandler(DomainException.class)
    public Mono<ApiResponse> handleDomainException(DomainException e, Locale locale) {
        String localizedMessage = messageSource.getMessage(e.getMessageKey(), e.getArgs(), locale);
        return Mono.just(ApiResponse.error(e.getErrorCode(), localizedMessage));
    }

    /**
     * Handle validation exceptions (e.g. @Valid failure).
     *
     * @param e WebExchangeBindException
     * @return ApiResponse containing validation error details
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ApiResponse> handleValidationException(WebExchangeBindException e) {
        String errorMessage = e.getFieldErrors().stream()
                .map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining("; "));
        return Mono.just(ApiResponse.error(501, errorMessage));
    }

    /**
     * Handle all uncaught exceptions that are not custom-defined.
     *
     * @param e the exception
     * @return standardized API response
     */
    @ExceptionHandler(Exception.class)
    public Mono<ApiResponse> handleGenericException(Exception e) {
        e.printStackTrace();
        return Mono.just(ApiResponse.error("Internal Server Error: " + e.getMessage()));
    }
}
