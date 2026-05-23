package com.springddd.application.service.auth.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

    @InjectMocks
    private CustomAccessDeniedHandler handler;

    @Mock
    private ServerWebExchange exchange;

    @Mock
    private ServerHttpResponse response;

    @Mock
    private AccessDeniedException accessDeniedException;

    @Test
    @DisplayName("handle 应返回 403 Forbidden JSON 响应")
    void handle_shouldReturn403Forbidden() {
        when(exchange.getResponse()).thenReturn(response);
        when(response.bufferFactory()).thenReturn(new DefaultDataBufferFactory());
        when(response.getHeaders()).thenReturn(new org.springframework.http.HttpHeaders());
        when(response.writeWith(any(Mono.class))).thenReturn(Mono.empty());

        StepVerifier.create(handler.handle(exchange, accessDeniedException))
                .verifyComplete();

        verify(response).setStatusCode(HttpStatus.FORBIDDEN);
        verify(response, atLeastOnce()).getHeaders();
    }
}
