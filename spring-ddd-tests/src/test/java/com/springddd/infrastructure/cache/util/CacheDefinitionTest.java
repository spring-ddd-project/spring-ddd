package com.springddd.infrastructure.cache.util;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class CacheDefinitionTest {

    @Test
    void testBuildKey() {
        CacheDefinition def = new CacheDefinition("user:%s:%d", Duration.ofMinutes(5));
        assertThat(def.buildKey("admin", 1)).isEqualTo("user:admin:1");
    }

    @Test
    void testOfWithTtl() {
        CacheDefinition def = CacheDefinition.of("test:%s", Duration.ofHours(1));
        assertThat(def.pattern()).isEqualTo("test:%s");
        assertThat(def.ttl()).isEqualTo(Duration.ofHours(1));
    }

    @Test
    void testOfWithoutTtl() {
        CacheDefinition def = CacheDefinition.of("test:%s");
        assertThat(def.pattern()).isEqualTo("test:%s");
        assertThat(def.ttl()).isNull();
    }
}
