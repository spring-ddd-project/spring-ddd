package com.springddd.infrastructure.cache.util;

import java.time.Duration;

public record CacheDefinition(String pattern, Duration ttl) {

    public String buildKey(Object... args) {
        return String.format(pattern, args);
    }

    public static CacheDefinition of(String pattern) {
        return new CacheDefinition(pattern, null);
    }

    public static CacheDefinition of(String pattern, Duration ttl) {
        return new CacheDefinition(pattern, ttl);
    }
}