package com.springddd.infrastructure.cache.util;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

class CacheDefinitionTest {

    @Test
    void shouldCreateCacheDefinitionWithPatternOnly() {
        CacheDefinition def = CacheDefinition.of("user:%s:id");
        assertEquals("user:%s:id", def.pattern());
        assertNull(def.ttl());
    }

    @Test
    void shouldCreateCacheDefinitionWithPatternAndTtl() {
        CacheDefinition def = CacheDefinition.of("user:%s:token", Duration.ofMinutes(30));
        assertEquals("user:%s:token", def.pattern());
        assertEquals(Duration.ofMinutes(30), def.ttl());
    }

    @Test
    void shouldBuildKeyWithArguments() {
        CacheDefinition def = CacheDefinition.of("user:%s:detail");
        String key = def.buildKey("123");
        assertEquals("user:123:detail", key);
    }

    @Test
    void shouldBuildKeyWithMultipleArguments() {
        CacheDefinition def = CacheDefinition.of("user:%s:files:%s");
        String key = def.buildKey("123", "456");
        assertEquals("user:123:files:456", key);
    }

    @Test
    void shouldBuildKeyWithNoArguments() {
        CacheDefinition def = CacheDefinition.of("all:users");
        String key = def.buildKey();
        assertEquals("all:users", key);
    }

    @Test
    void equals_shouldWorkForSameValues() {
        CacheDefinition def1 = CacheDefinition.of("user:%s:id", Duration.ofHours(1));
        CacheDefinition def2 = CacheDefinition.of("user:%s:id", Duration.ofHours(1));
        assertEquals(def1, def2);
    }

    @Test
    void toString_shouldReturnValueAsString() {
        CacheDefinition def = CacheDefinition.of("user:%s:token");
        String str = def.toString();
        assertTrue(str.contains("CacheDefinition"));
    }
}
