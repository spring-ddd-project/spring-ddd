package com.springddd.infrastructure.cache.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoggingCacheDecoratorTest {

    @Mock
    private ReactiveRedisCacheHelper delegate;

    private LoggingCacheDecorator decorator;

    @BeforeEach
    void setUp() {
        decorator = new LoggingCacheDecorator(delegate);
    }

    @Test
    @DisplayName("getOrLoad 应委托给 delegate 并返回结果")
    void getOrLoad_shouldDelegateAndReturnResult() {
        when(delegate.getOrLoad(anyString(), any(), any(), any())).thenReturn(Mono.just("value"));

        StepVerifier.create(decorator.getOrLoad("key", String.class, Duration.ofMinutes(5),
                        () -> Mono.just("db")))
                .expectNext("value")
                .verifyComplete();

        verify(delegate, times(1)).getOrLoad(eq("key"), eq(String.class), any(), any());
    }

    @Test
    @DisplayName("getOrLoad 当值为 null 时应委托给 delegate")
    void getOrLoad_whenNullValue_shouldDelegate() {
        when(delegate.getOrLoad(anyString(), any(), any(), any())).thenReturn(Mono.empty());

        StepVerifier.create(decorator.getOrLoad("key", String.class, Duration.ofMinutes(5),
                        () -> Mono.empty()))
                .verifyComplete();

        verify(delegate, times(1)).getOrLoad(anyString(), any(), any(), any());
    }

    @Test
    @DisplayName("setCache 应委托给 delegate 并返回结果")
    void setCache_shouldDelegateAndReturnResult() {
        when(delegate.setCache(anyString(), any(), any())).thenReturn(Mono.just(true));

        StepVerifier.create(decorator.setCache("key", "value", Duration.ofMinutes(5)))
                .expectNext(true)
                .verifyComplete();

        verify(delegate, times(1)).setCache("key", "value", Duration.ofMinutes(5));
    }

    @Test
    @DisplayName("getCache 应委托给 delegate 并返回结果")
    void getCache_shouldDelegateAndReturnResult() {
        when(delegate.getCache(anyString(), any())).thenReturn(Mono.just("value"));

        StepVerifier.create(decorator.getCache("key", String.class))
                .expectNext("value")
                .verifyComplete();

        verify(delegate, times(1)).getCache("key", String.class);
    }

    @Test
    @DisplayName("deleteCache 应委托给 delegate")
    void deleteCache_shouldDelegate() {
        when(delegate.deleteCache(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(decorator.deleteCache("key"))
                .verifyComplete();

        verify(delegate, times(1)).deleteCache("key");
    }
}
