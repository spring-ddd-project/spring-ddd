package com.springddd.application.service.auth.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class CustomAuthenticationEntryPointTest {

    private final CustomAuthenticationEntryPoint entryPoint = new CustomAuthenticationEntryPoint();

    @Test
    void commence_shouldReturnUnauthorizedStatus() {
        MockServerHttpResponse response = new MockServerHttpResponse();
        MockServerWebExchange exchange = MockServerWebExchange.from("/test");

        entryPoint.commence(exchange, new RuntimeException("Test exception"))
                .block();

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
