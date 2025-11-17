package com.springddd.infrastructure.cache.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.function.Supplier;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class LoggingCacheDecorator implements CacheProcessor {
    private final ReactiveRedisCacheHelper delegate;

    @Override
    public <R> Mono<R> getOrLoad(String key, Class<?> clazz, Duration ttl, Supplier<Mono<R>> dbLoader) {
        log.info("Cache access: key={}", key);
        return delegate.getOrLoad(key, clazz, ttl, dbLoader)
                .doOnSuccess(v -> {
                    if (v != null) log.info("Cache hit: key={}", key);
                    else log.info("Cache miss or null mark: key={}", key);
                });
    }

    @Override
    public <T> Mono<Boolean> setCache(String key, T value, Duration ttl) {
        log.info("Cache set: key={}, ttl={}", key, ttl);
        return delegate.setCache(key, value, ttl);
    }

    @Override
    public <T> Mono<T> getCache(String key, Class<T> clazz) {
        log.info("Cache get: key={}", key);
        return delegate.getCache(key, clazz);
    }

    @Override
    public Mono<Void> deleteCache(String key) {
        log.info("Cache delete: key={}", key);
        return delegate.deleteCache(key);
    }
}
