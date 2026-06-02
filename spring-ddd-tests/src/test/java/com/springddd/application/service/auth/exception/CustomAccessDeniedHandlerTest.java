package com.springddd.application.service.auth.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomAccessDeniedHandlerTest {

    private final CustomAccessDeniedHandler handler = new CustomAccessDeniedHandler();

    @Mock
    private ServerWebExchange exchange;

    @Mock
    private ServerHttpResponse response;

    @Mock
    private AccessDeniedException denied;

    @Test
    void handle_shouldReturn403JsonResponse() {
        DataBufferFactory bufferFactory = new DefaultDataBufferFactory();

        when(exchange.getResponse()).thenReturn(response);
        when(response.getHeaders()).thenReturn(new org.springframework.http.HttpHeaders());
        when(response.bufferFactory()).thenReturn(bufferFactory);
        when(response.writeWith(any())).thenReturn(Mono.empty());

        StepVerifier.create(handler.handle(exchange, denied))
                .verifyComplete();

        verify(response).setStatusCode(HttpStatus.FORBIDDEN);
    }
}
