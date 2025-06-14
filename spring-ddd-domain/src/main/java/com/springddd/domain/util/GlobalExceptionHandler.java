package com.springddd.domain.util;

import com.springddd.domain.exception.DomainException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Component
@RestControllerAdvice
@Order(-1)
public class GlobalExceptionHandler {

    /**
     * 自定义异常
     *
     * @param e 异常
     * @return 统一返回
     */
    @ExceptionHandler(DomainException.class)
    public Mono<ApiResponse> handleBadRequestException(DomainException e) {
        return Mono.just(ApiResponse.error(e.getErrorCode(), e.getMessage()));
    }


    /**
     * 全局统一异常，即自定义异常之外的所有异常
     *
     * @param e 异常
     * @return 统一返回
     */
    @ExceptionHandler(Exception.class)
    public Mono<ApiResponse> handleGenericException(Exception e) {
        e.printStackTrace();
        return Mono.just(ApiResponse.error("Internal Server Error: " + e.getMessage()));
    }
}
