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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultCacheManagerTest {

    @Mock
    private CacheProcessor cacheProcessor;

    private DefaultCacheManager cacheManager;

    @BeforeEach
    void setUp() {
        cacheManager = new DefaultCacheManager(cacheProcessor);
    }

    @Test
    @DisplayName("getCache 应委托给 cacheProcessor 并返回结果")
    void getCache_shouldDelegateAndReturnResult() {
        when(cacheProcessor.getCache(anyString(), any())).thenReturn(Mono.just("cachedValue"));

        StepVerifier.create(cacheManager.getCache("key", String.class))
                .expectNext("cachedValue")
                .verifyComplete();

        verify(cacheProcessor, times(1)).getCache("key", String.class);
    }

    @Test
    @DisplayName("getCache 当缓存未命中时应返回 empty")
    void getCache_whenCacheMiss_shouldReturnEmpty() {
        when(cacheProcessor.getCache(anyString(), any())).thenReturn(Mono.empty());

        StepVerifier.create(cacheManager.getCache("key", String.class))
                .verifyComplete();
    }

    @Test
    @DisplayName("setCache 应委托给 cacheProcessor 并设置 TTL 为秒")
    void setCache_shouldDelegateWithSecondsTtl() {
        when(cacheProcessor.setCache(anyString(), any(), any())).thenReturn(Mono.just(true));

        StepVerifier.create(cacheManager.setCache("key", "value", 120L))
                .verifyComplete();

        verify(cacheProcessor, times(1)).setCache(eq("key"), eq("value"), argThat(d -> d.getSeconds() == 120));
    }

    @Test
    @DisplayName("removeCache 应委托给 cacheProcessor 并返回 true")
    void removeCache_shouldDelegateAndReturnTrue() {
        when(cacheProcessor.deleteCache(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(cacheManager.removeCache("key"))
                .expectNext(true)
                .verifyComplete();

        verify(cacheProcessor, times(1)).deleteCache("key");
    }
}
