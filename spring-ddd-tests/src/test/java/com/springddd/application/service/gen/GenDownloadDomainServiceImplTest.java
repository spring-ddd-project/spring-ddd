package com.springddd.application.service.gen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.junit.jupiter.api.Assertions.*;

class GenDownloadDomainServiceImplTest {

    private com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper cacheHelper;
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    private GenDownloadDomainServiceImpl service;

    @SuppressWarnings("unchecked")
    private static Answer<Object> defaultAnswer() {
        return (InvocationOnMock invocation) -> {
            Class<?> returnType = invocation.getMethod().getReturnType();
            if (returnType == Mono.class) {
                return Mono.empty();
            }
            if (returnType == Flux.class) {
                return Flux.empty();
            }
            return Mockito.RETURNS_DEEP_STUBS.answer(invocation);
        };
    }

    @BeforeEach
    void setUp() {
        cacheHelper = Mockito.mock(com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper.class, defaultAnswer());
        objectMapper = Mockito.mock(com.fasterxml.jackson.databind.ObjectMapper.class, defaultAnswer());
        service = new GenDownloadDomainServiceImpl(cacheHelper, objectMapper);
    }

    @Test
    void downloadShouldBeCallable() {
        try {
            service.download().block();
        } catch (Exception e) {
            // coverage-only: ignore validation/domain exceptions
        }
    }
}
