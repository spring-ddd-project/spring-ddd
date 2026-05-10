package com.springddd.infrastructure.cache.util;

import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CompositeCacheProcessor implements CacheProcessor {
    private final List<CacheProcessor> processors = new ArrayList<>();

    public void addProcessor(CacheProcessor processor) {
        processors.add(processor);
    }

    @Override
    public <R> Mono<R> getOrLoad(String key, Class<?> clazz, Duration ttl, Supplier<Mono<R>> dbLoader) {
        Mono<R> result = Mono.empty();
        for (CacheProcessor processor : processors) {
            result = result.switchIfEmpty(processor.getOrLoad(key, clazz, ttl, dbLoader));
        }
        return result;
    }

    @Override
    public <T> Mono<Boolean> setCache(String key, T value, Duration ttl) {
        return Mono.when(processors.stream().map(p -> p.setCache(key, value, ttl)).toArray(Mono[]::new)).thenReturn(true);
    }

    @Override
    public <T> Mono<T> getCache(String key, Class<T> clazz) {
        Mono<T> result = Mono.empty();
        for (CacheProcessor processor : processors) {
            result = result.switchIfEmpty(processor.getCache(key, clazz));
        }
        return result;
    }

    @Override
    public Mono<Void> deleteCache(String key) {
        return Mono.when(processors.stream().map(p -> p.deleteCache(key)).toArray(Mono[]::new));
    }
}
