package com.springddd.infrastructure.cache.util;

import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;

public class CompositeCacheProcessor implements CacheProcessor {
    private final List<CacheProcessor> processors = new ArrayList<>();

    public void addProcessor(CacheProcessor processor) {
        processors.add(processor);
    }

    @Override
    public <T> Mono<T> get(String key, Class<T> clazz) {
        Mono<T> result = Mono.empty();
        for (CacheProcessor processor : processors) {
            result = result.switchIfEmpty(processor.get(key, clazz));
        }
        return result;
    }

    @Override
    public Mono<Void> put(String key, Object value, long timeout) {
        return Mono.when(processors.stream().map(p -> p.put(key, value, timeout)).toArray(Mono[]::new));
    }

    @Override
    public Mono<Boolean> delete(String key) {
        return Mono.zip(processors.stream().map(p -> p.delete(key)).toArray(Mono[]::new),
                results -> {
                    for (Object res : results) {
                        if (Boolean.TRUE.equals(res)) return true;
                    }
                    return false;
                });
    }
}
