package com.springddd.infrastructure.cache.bridge;

import com.springddd.infrastructure.cache.util.CacheProcessor;
import reactor.core.publisher.Mono;

public abstract class AbstractCacheManager {
    protected final CacheProcessor cacheProcessor;

    public AbstractCacheManager(CacheProcessor cacheProcessor) {
        this.cacheProcessor = cacheProcessor;
    }

    public abstract <T> Mono<T> getCache(String key, Class<T> clazz);
    public abstract Mono<Void> setCache(String key, Object value, long timeout);
    public abstract Mono<Boolean> removeCache(String key);
}
