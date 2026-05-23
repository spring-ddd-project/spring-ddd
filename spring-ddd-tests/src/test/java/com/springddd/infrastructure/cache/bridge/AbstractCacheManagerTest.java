package com.springddd.infrastructure.cache.bridge;

import com.springddd.infrastructure.cache.util.CacheProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbstractCacheManagerTest {

    @Mock
    private CacheProcessor cacheProcessor;

    private AbstractCacheManager cacheManager;

    @BeforeEach
    void setUp() {
        cacheManager = new AbstractCacheManager(cacheProcessor) {
            @Override
            public <T> Mono<T> getCache(String key, Class<T> clazz) {
                return cacheProcessor.getCache(key, clazz);
            }

            @Override
            public Mono<Void> setCache(String key, Object value, long timeout) {
                return cacheProcessor.setCache(key, value, java.time.Duration.ofSeconds(timeout)).then();
            }

            @Override
            public Mono<Boolean> removeCache(String key) {
                return cacheProcessor.deleteCache(key).thenReturn(true);
            }
        };
    }

    @Test
    @DisplayName("构造函数应正确注入 cacheProcessor")
    void constructor_shouldInjectCacheProcessor() {
        assertThat(cacheManager).isNotNull();
    }

    @Test
    @DisplayName("getCache 应委托给 cacheProcessor")
    void getCache_shouldDelegateToProcessor() {
        when(cacheProcessor.getCache(anyString(), any())).thenReturn(Mono.just("value"));

        StepVerifier.create(cacheManager.getCache("key", String.class))
                .expectNext("value")
                .verifyComplete();

        verify(cacheProcessor, times(1)).getCache("key", String.class);
    }

    @Test
    @DisplayName("setCache 应委托给 cacheProcessor")
    void setCache_shouldDelegateToProcessor() {
        when(cacheProcessor.setCache(anyString(), any(), any())).thenReturn(Mono.just(true));

        StepVerifier.create(cacheManager.setCache("key", "value", 60L))
                .verifyComplete();

        verify(cacheProcessor, times(1)).setCache(eq("key"), eq("value"), any());
    }

    @Test
    @DisplayName("removeCache 应委托给 cacheProcessor 并返回 true")
    void removeCache_shouldDelegateToProcessorAndReturnTrue() {
        when(cacheProcessor.deleteCache(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(cacheManager.removeCache("key"))
                .expectNext(true)
                .verifyComplete();

        verify(cacheProcessor, times(1)).deleteCache("key");
    }
}
