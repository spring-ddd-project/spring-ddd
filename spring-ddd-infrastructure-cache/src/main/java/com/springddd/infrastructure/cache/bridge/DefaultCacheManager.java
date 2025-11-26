package com.springddd.infrastructure.cache.bridge;

import com.springddd.infrastructure.cache.util.CacheProcessor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DefaultCacheManager extends AbstractCacheManager {

    public DefaultCacheManager(CacheProcessor cacheProcessor) {
        super(cacheProcessor);
    }

    @Override
    public <T> Mono<T> getCache(String key, Class<T> clazz) {
        return cacheProcessor.get(key, clazz);
    }

    @Override
    public Mono<Void> setCache(String key, Object value, long timeout) {
        return cacheProcessor.put(key, value, timeout);
    }

    @Override
    public Mono<Boolean> removeCache(String key) {
        return cacheProcessor.delete(key);
    }
}
