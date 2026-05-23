package com.springddd.domain.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebfluxGlobalErrorHandlerTest {

    @Mock
    private ServerWebExchange exchange;

    @Mock
    private ServerHttpResponse response;

    @Mock
    private DataBufferFactory bufferFactory;

    @InjectMocks
    private WebfluxGlobalErrorHandler handler;

    private ObjectMapper objectMapper;
    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        headers = new HttpHeaders();
        handler = new WebfluxGlobalErrorHandler(objectMapper);
    }

    @Test
    @DisplayName("handle 当 response 已提交时应返回 error mono")
    void handle_whenResponseCommitted_shouldReturnError() {
        when(exchange.getResponse()).thenReturn(response);
        when(response.isCommitted()).thenReturn(true);

        RuntimeException ex = new RuntimeException("test");
        StepVerifier.create(handler.handle(exchange, ex))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    @DisplayName("handle ResponseStatusException 应设置对应状态码")
    void handle_whenResponseStatusException_shouldSetStatus() {
        when(exchange.getResponse()).thenReturn(response);
        when(response.isCommitted()).thenReturn(false);
        when(response.getHeaders()).thenReturn(headers);
        when(response.bufferFactory()).thenReturn(bufferFactory);
        when(bufferFactory.wrap(any(byte[].class))).thenReturn(mock(DataBuffer.class));
        when(response.writeWith(any())).thenReturn(Mono.empty());

        ResponseStatusException ex = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");

        StepVerifier.create(handler.handle(exchange, ex))
                .verifyComplete();

        verify(response).setStatusCode(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("handle AccessDeniedException 应返回 403")
    void handle_whenAccessDenied_shouldReturn403() {
        when(exchange.getResponse()).thenReturn(response);
        when(response.isCommitted()).thenReturn(false);
        when(response.getHeaders()).thenReturn(headers);
        when(response.bufferFactory()).thenReturn(bufferFactory);
        when(bufferFactory.wrap(any(byte[].class))).thenReturn(mock(DataBuffer.class));
        when(response.writeWith(any())).thenReturn(Mono.empty());

        AccessDeniedException ex = new AccessDeniedException("Access denied");

        StepVerifier.create(handler.handle(exchange, ex))
                .verifyComplete();

        verify(response).setStatusCode(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("handle AuthenticationException 应返回 401")
    void handle_whenAuthenticationException_shouldReturn401() {
        when(exchange.getResponse()).thenReturn(response);
        when(response.isCommitted()).thenReturn(false);
        when(response.getHeaders()).thenReturn(headers);
        when(response.bufferFactory()).thenReturn(bufferFactory);
        when(bufferFactory.wrap(any(byte[].class))).thenReturn(mock(DataBuffer.class));
        when(response.writeWith(any())).thenReturn(Mono.empty());

        AuthenticationException ex = new AuthenticationException("Unauthorized") {};

        StepVerifier.create(handler.handle(exchange, ex))
                .verifyComplete();

        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("handle 通用异常应返回 500")
    void handle_whenGenericException_shouldReturn500() {
        when(exchange.getResponse()).thenReturn(response);
        when(response.isCommitted()).thenReturn(false);
        when(response.getHeaders()).thenReturn(headers);
        when(response.bufferFactory()).thenReturn(bufferFactory);
        when(bufferFactory.wrap(any(byte[].class))).thenReturn(mock(DataBuffer.class));
        when(response.writeWith(any())).thenReturn(Mono.empty());

        RuntimeException ex = new RuntimeException("Internal error");

        StepVerifier.create(handler.handle(exchange, ex))
                .verifyComplete();

        verify(response).setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("handle 当 ObjectMapper 序列化失败时应返回 fallback JSON")
    void handle_whenObjectMapperThrows_shouldReturnFallback() throws JsonProcessingException {
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        when(mockObjectMapper.writeValueAsBytes(any())).thenThrow(new JsonProcessingException("Serialization failed") {});
        WebfluxGlobalErrorHandler errorHandler = new WebfluxGlobalErrorHandler(mockObjectMapper);

        when(exchange.getResponse()).thenReturn(response);
        when(response.isCommitted()).thenReturn(false);
        when(response.getHeaders()).thenReturn(headers);
        when(response.bufferFactory()).thenReturn(bufferFactory);
        when(bufferFactory.wrap(any(byte[].class))).thenReturn(mock(DataBuffer.class));
        when(response.writeWith(any())).thenReturn(Mono.empty());

        RuntimeException ex = new RuntimeException("Internal error");

        StepVerifier.create(errorHandler.handle(exchange, ex))
                .verifyComplete();

        verify(response).setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
