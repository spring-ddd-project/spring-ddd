package com.springddd.application.service.auth.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomAccessDeniedHandlerTest {

    private final CustomAccessDeniedHandler handler = new CustomAccessDeniedHandler();

    private MockServerWebExchange createExchange() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
        return MockServerWebExchange.from(request);
    }

    @Test
    void handle_shouldReturnForbiddenStatus() {
        MockServerWebExchange exchange = createExchange();
        ServerHttpResponse response = exchange.getResponse();

        AccessDeniedException deniedException = new AccessDeniedException("Test access denied");

        Mono<Void> result = handler.handle(exchange, deniedException);

        StepVerifier.create(result)
                .verifyComplete();

        assertEquals(org.springframework.http.HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void handle_shouldSetJsonContentType() {
        MockServerWebExchange exchange = createExchange();
        ServerHttpResponse response = exchange.getResponse();

        AccessDeniedException deniedException = new AccessDeniedException("Test access denied");

        handler.handle(exchange, deniedException).block();

        assertEquals(org.springframework.http.MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test
    void handle_shouldWriteJsonBody() {
        MockServerWebExchange exchange = createExchange();
        MockServerHttpResponse response = (MockServerHttpResponse) exchange.getResponse();

        AccessDeniedException deniedException = new AccessDeniedException("Test access denied");

        handler.handle(exchange, deniedException).block();

        String body = response.getBodyAsString().block();
        assertNotNull(body);
        assertTrue(body.contains("\"code\": 403"));
        assertTrue(body.contains("Access to this resource is denied"));
    }
}
