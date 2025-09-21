package com.springddd.application.service.auth.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationEntryPointTest {

    private final CustomAuthenticationEntryPoint entryPoint = new CustomAuthenticationEntryPoint();

    private MockServerWebExchange createExchange() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
        return MockServerWebExchange.from(request);
    }

    @Test
    void commence_shouldReturnUnauthorizedStatus() {
        MockServerWebExchange exchange = createExchange();
        ServerHttpResponse response = exchange.getResponse();

        AuthenticationException authException = new AuthenticationException("Test auth exception") {};

        Mono<Void> result = entryPoint.commence(exchange, authException);

        StepVerifier.create(result)
                .verifyComplete();

        assertEquals(org.springframework.http.HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void commence_shouldSetJsonContentType() {
        MockServerWebExchange exchange = createExchange();
        ServerHttpResponse response = exchange.getResponse();

        AuthenticationException authException = new AuthenticationException("Test auth exception") {};

        entryPoint.commence(exchange, authException).block();

        assertEquals(org.springframework.http.MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test
    void commence_shouldWriteJsonBody() {
        MockServerWebExchange exchange = createExchange();
        MockServerHttpResponse response = (MockServerHttpResponse) exchange.getResponse();

        AuthenticationException authException = new AuthenticationException("Test auth exception") {};

        entryPoint.commence(exchange, authException).block();

        String body = response.getBodyAsString().block();
        assertNotNull(body);
        assertTrue(body.contains("\"code\": 401"));
        assertTrue(body.contains("Please log in first"));
    }
}
