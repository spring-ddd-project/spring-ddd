package com.springddd.infrastructure.cache.bridge;

import com.springddd.infrastructure.cache.util.CacheProcessor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class DefaultCacheManager extends AbstractCacheManager {

    public DefaultCacheManager(CacheProcessor cacheProcessor) {
        super(cacheProcessor);
    }

    @Override
    public <T> Mono<T> getCache(String key, Class<T> clazz) {
        return cacheProcessor.getCache(key, clazz);
    }

    @Override
    public Mono<Void> setCache(String key, Object value, long timeout) {
        return cacheProcessor.setCache(key, value, Duration.ofSeconds(timeout)).then();
    }

    @Override
    public Mono<Boolean> removeCache(String key) {
        return cacheProcessor.deleteCache(key).thenReturn(true);
    }
}
