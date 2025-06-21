package com.springddd.infrastructure.cache.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class ReactiveRedisCacheHelper {

    private static final String NULL_MARK = "__NULL__";

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper;

    // Unified cache access with cache miss fallback, null-value protection, TTL, and error fallback
    public <R> Mono<R> getOrLoad(String key, Class<?> clazz, Duration ttl, Supplier<Mono<R>> dbLoader) {
        return redisTemplate.opsForValue().get(key)
                .flatMap(json -> deserialize(json, clazz).map(obj -> (R) obj))
                .switchIfEmpty(
                        dbLoader.get()
                                .flatMap(value -> {
                                    if (value == null) {
                                        return redisTemplate.opsForValue()
                                                .set(key, NULL_MARK, Duration.ofMinutes(1))
                                                .then(Mono.empty());
                                    }
                                    return serialize(value)
                                            .flatMap(json -> redisTemplate.opsForValue()
                                                    .set(key, json, ttl)
                                                    .thenReturn(value));
                                })
                )
                .publishOn(Schedulers.boundedElastic())
                .onErrorResume(e -> dbLoader.get());
    }


    // Batch cache retrieval (processed one-by-one)
    public <T> Flux<T> getOrLoadBatch(
            List<String> keys,
            Function<String, Mono<T>> dbLoader,
            Class<T> clazz,
            Duration ttl) {

        return Flux.fromIterable(keys)
                .flatMap(key -> getOrLoad(key, clazz, ttl, () -> dbLoader.apply(key)));
    }

    // Rate limiting based on Redis INCR (simple token count)
    public Mono<Boolean> isAllowed(String key, int limit, Duration window) {
        String redisKey = "rate:" + key;
        return redisTemplate.opsForValue().increment(redisKey)
                .flatMap(count -> {
                    if (count == 1) {
                        return redisTemplate.expire(redisKey, window).thenReturn(true);
                    }
                    return Mono.just(count <= limit);
                });
    }

    // Write value to cache
    public <T> Mono<Boolean> setCache(String key, T value, Duration ttl) {
        return serialize(value)
                .flatMap(json -> redisTemplate.opsForValue().set(key, json, ttl));
    }

    // Read from cache (no DB fallback)
    public <T> Mono<T> getCache(String key, Class<T> clazz) {
        return redisTemplate.opsForValue().get(key)
                .flatMap(json -> deserialize(json, clazz));
    }

    // Build unified cache key with namespace
    public String buildKey(String namespace, String id) {
        return namespace + ":" + id;
    }

    private <T> Mono<T> deserialize(String json, Class<T> clazz) {
        try {
            if (json == null || json.isBlank() || NULL_MARK.equals(json)) {
                return Mono.empty();
            }
            return Mono.just(objectMapper.readValue(json, clazz));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    private <T> Mono<String> serialize(T value) {
        try {
            return Mono.just(value == null ? NULL_MARK : objectMapper.writeValueAsString(value));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    // Delete cache entry by key
    public Mono<Void> deleteCache(String key) {
        return redisTemplate.keys(key)
                .collectList()
                .flatMapMany(Flux::fromIterable)
                .flatMap(redisTemplate::delete)
                .then();
    }
}
