package com.springddd.domain.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class WebfluxGlobalErrorHandlerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ServerWebExchange exchange;

    @Mock
    private ServerHttpResponse response;

    @Mock
    private DataBufferFactory bufferFactory;

    @Mock
    private DataBuffer dataBuffer;

    private WebfluxGlobalErrorHandler handler;

    @BeforeEach
    void setUp() {
        handler = new WebfluxGlobalErrorHandler(objectMapper);
        when(exchange.getResponse()).thenReturn(response);
        when(response.bufferFactory()).thenReturn(bufferFactory);
        when(bufferFactory.wrap(any(byte[].class))).thenReturn(dataBuffer);
    }

    @Test
    void handle_shouldReturnErrorForCommittedResponse() {
        when(response.isCommitted()).thenReturn(true);

        RuntimeException ex = new RuntimeException("error");
        StepVerifier.create(handler.handle(exchange, ex))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void handle_shouldReturnInternalServerErrorForGenericException() throws Exception {
        when(response.isCommitted()).thenReturn(false);
        when(response.getHeaders()).thenReturn(new org.springframework.http.HttpHeaders());
        when(objectMapper.writeValueAsBytes(any())).thenReturn("{\"code\":500}".getBytes());
        when(response.writeWith(any())).thenReturn(Mono.empty());

        StepVerifier.create(handler.handle(exchange, new RuntimeException("fail")))
                .verifyComplete();

        verify(response).setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void handle_shouldReturnStatusForResponseStatusException() throws Exception {
        when(response.isCommitted()).thenReturn(false);
        when(response.getHeaders()).thenReturn(new org.springframework.http.HttpHeaders());
        when(objectMapper.writeValueAsBytes(any())).thenReturn("{\"code\":404}".getBytes());
        when(response.writeWith(any())).thenReturn(Mono.empty());

        StepVerifier.create(handler.handle(exchange, new ResponseStatusException(HttpStatus.NOT_FOUND, "not found")))
                .verifyComplete();

        verify(response).setStatusCode(HttpStatus.NOT_FOUND);
    }

    @Test
    void handle_shouldReturnForbiddenForAccessDenied() throws Exception {
        when(response.isCommitted()).thenReturn(false);
        when(response.getHeaders()).thenReturn(new org.springframework.http.HttpHeaders());
        when(objectMapper.writeValueAsBytes(any())).thenReturn("{\"code\":403}".getBytes());
        when(response.writeWith(any())).thenReturn(Mono.empty());

        StepVerifier.create(handler.handle(exchange, new AccessDeniedException("denied")))
                .verifyComplete();

        verify(response).setStatusCode(HttpStatus.FORBIDDEN);
    }

    @Test
    void handle_shouldReturnUnauthorizedForAuthException() throws Exception {
        when(response.isCommitted()).thenReturn(false);
        when(response.getHeaders()).thenReturn(new org.springframework.http.HttpHeaders());
        when(objectMapper.writeValueAsBytes(any())).thenReturn("{\"code\":401}".getBytes());
        when(response.writeWith(any())).thenReturn(Mono.empty());

        StepVerifier.create(handler.handle(exchange, new AuthenticationException("auth failed") {}))
                .verifyComplete();

        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void handle_shouldUseFallbackWhenSerializationFails() throws Exception {
        when(response.isCommitted()).thenReturn(false);
        when(response.getHeaders()).thenReturn(new org.springframework.http.HttpHeaders());
        when(objectMapper.writeValueAsBytes(any())).thenThrow(new JsonProcessingException("fail") {});
        when(response.writeWith(any())).thenReturn(Mono.empty());

        StepVerifier.create(handler.handle(exchange, new RuntimeException("fail")))
                .verifyComplete();
    }

    @Test
    void handle_shouldReturn500WhenResponseStatusExceptionHasUnknownCode() throws Exception {
        when(response.isCommitted()).thenReturn(false);
        when(response.getHeaders()).thenReturn(new org.springframework.http.HttpHeaders());
        when(objectMapper.writeValueAsBytes(any())).thenReturn("{\"code\":500}".getBytes());
        when(response.writeWith(any())).thenReturn(Mono.empty());

        ResponseStatusException ex = new ResponseStatusException(
                org.springframework.http.HttpStatusCode.valueOf(999), "unknown status");

        StepVerifier.create(handler.handle(exchange, ex))
                .verifyComplete();

        verify(response).setStatusCode((HttpStatus) null);
    }
}
