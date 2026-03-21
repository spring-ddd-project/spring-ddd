package com.springddd.domain.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebfluxGlobalErrorHandlerTest {

    private DataBufferFactory bufferFactory = new DefaultDataBufferFactory();

    @Test
    void testHandleGenericException() throws Exception {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        ServerHttpResponse response = mock(ServerHttpResponse.class);

        when(exchange.getResponse()).thenReturn(response);
        when(response.isCommitted()).thenReturn(false);
        when(response.getHeaders()).thenReturn(new org.springframework.http.HttpHeaders());
        when(response.bufferFactory()).thenReturn(bufferFactory);
        when(objectMapper.writeValueAsBytes(any())).thenReturn("{\"code\":500}".getBytes());
        when(response.writeWith(any())).thenReturn(Mono.empty());

        WebfluxGlobalErrorHandler handler = new WebfluxGlobalErrorHandler(objectMapper);
        RuntimeException ex = new RuntimeException("Test error");

        Mono<Void> mono = handler.handle(exchange, ex);

        assertNotNull(mono);
        StepVerifier.create(mono).verifyComplete();
        verify(response).setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void testHandleResponseStatusException() throws Exception {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        ServerHttpResponse response = mock(ServerHttpResponse.class);

        when(exchange.getResponse()).thenReturn(response);
        when(response.isCommitted()).thenReturn(false);
        when(response.getHeaders()).thenReturn(new org.springframework.http.HttpHeaders());
        when(response.bufferFactory()).thenReturn(bufferFactory);
        when(objectMapper.writeValueAsBytes(any())).thenReturn("{\"code\":400}".getBytes());
        when(response.writeWith(any())).thenReturn(Mono.empty());

        WebfluxGlobalErrorHandler handler = new WebfluxGlobalErrorHandler(objectMapper);
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");

        Mono<Void> mono = handler.handle(exchange, ex);

        assertNotNull(mono);
        StepVerifier.create(mono).verifyComplete();
        verify(response).setStatusCode(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testHandleAccessDeniedException() throws Exception {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        ServerHttpResponse response = mock(ServerHttpResponse.class);

        when(exchange.getResponse()).thenReturn(response);
        when(response.isCommitted()).thenReturn(false);
        when(response.getHeaders()).thenReturn(new org.springframework.http.HttpHeaders());
        when(response.bufferFactory()).thenReturn(bufferFactory);
        when(objectMapper.writeValueAsBytes(any())).thenReturn("{\"code\":403}".getBytes());
        when(response.writeWith(any())).thenReturn(Mono.empty());

        WebfluxGlobalErrorHandler handler = new WebfluxGlobalErrorHandler(objectMapper);
        org.springframework.security.access.AccessDeniedException ex =
                new org.springframework.security.access.AccessDeniedException("Access denied");

        Mono<Void> mono = handler.handle(exchange, ex);

        assertNotNull(mono);
        StepVerifier.create(mono).verifyComplete();
        verify(response).setStatusCode(HttpStatus.FORBIDDEN);
    }

    @Test
    void testHandleAuthenticationException() throws Exception {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        ServerHttpResponse response = mock(ServerHttpResponse.class);

        when(exchange.getResponse()).thenReturn(response);
        when(response.isCommitted()).thenReturn(false);
        when(response.getHeaders()).thenReturn(new org.springframework.http.HttpHeaders());
        when(response.bufferFactory()).thenReturn(bufferFactory);
        when(objectMapper.writeValueAsBytes(any())).thenReturn("{\"code\":401}".getBytes());
        when(response.writeWith(any())).thenReturn(Mono.empty());

        WebfluxGlobalErrorHandler handler = new WebfluxGlobalErrorHandler(objectMapper);
        org.springframework.security.core.AuthenticationException ex =
                new org.springframework.security.core.AuthenticationException("Unauthorized") {};

        Mono<Void> mono = handler.handle(exchange, ex);

        assertNotNull(mono);
        StepVerifier.create(mono).verifyComplete();
        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void testHandleWhenResponseIsCommitted() {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        ServerHttpResponse response = mock(ServerHttpResponse.class);

        when(exchange.getResponse()).thenReturn(response);
        when(response.isCommitted()).thenReturn(true);

        WebfluxGlobalErrorHandler handler = new WebfluxGlobalErrorHandler(objectMapper);
        RuntimeException ex = new RuntimeException("Test error");

        Mono<Void> mono = handler.handle(exchange, ex);

        StepVerifier.create(mono)
                .expectError(RuntimeException.class)
                .verify();

        verify(response, never()).setStatusCode(any());
    }

    @Test
    void testHandleJsonProcessingExceptionFallback() throws Exception {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        ServerHttpResponse response = mock(ServerHttpResponse.class);

        when(exchange.getResponse()).thenReturn(response);
        when(response.isCommitted()).thenReturn(false);
        when(response.getHeaders()).thenReturn(new org.springframework.http.HttpHeaders());
        when(response.bufferFactory()).thenReturn(bufferFactory);
        when(objectMapper.writeValueAsBytes(any()))
                .thenThrow(new com.fasterxml.jackson.core.JsonProcessingException("JSON error") {});
        when(response.writeWith(any())).thenReturn(Mono.empty());

        WebfluxGlobalErrorHandler handler = new WebfluxGlobalErrorHandler(objectMapper);
        RuntimeException ex = new RuntimeException("Test error");

        Mono<Void> mono = handler.handle(exchange, ex);

        assertNotNull(mono);
        StepVerifier.create(mono).verifyComplete();
        verify(response).setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void testHandleNotFoundException() throws Exception {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        ServerHttpResponse response = mock(ServerHttpResponse.class);

        when(exchange.getResponse()).thenReturn(response);
        when(response.isCommitted()).thenReturn(false);
        when(response.getHeaders()).thenReturn(new org.springframework.http.HttpHeaders());
        when(response.bufferFactory()).thenReturn(bufferFactory);
        when(objectMapper.writeValueAsBytes(any())).thenReturn("{\"code\":404}".getBytes());
        when(response.writeWith(any())).thenReturn(Mono.empty());

        WebfluxGlobalErrorHandler handler = new WebfluxGlobalErrorHandler(objectMapper);
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");

        Mono<Void> mono = handler.handle(exchange, ex);

        assertNotNull(mono);
        StepVerifier.create(mono).verifyComplete();
        verify(response).setStatusCode(HttpStatus.NOT_FOUND);
    }
}
