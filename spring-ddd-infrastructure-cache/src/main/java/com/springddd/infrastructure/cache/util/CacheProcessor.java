package com.springddd.infrastructure.cache.util;

import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.function.Supplier;

public interface CacheProcessor {
    <R> Mono<R> getOrLoad(String key, Class<?> clazz, Duration ttl, Supplier<Mono<R>> dbLoader);
    <T> Mono<Boolean> setCache(String key, T value, Duration ttl);
    <T> Mono<T> getCache(String key, Class<T> clazz);
    Mono<Void> deleteCache(String key);
}
