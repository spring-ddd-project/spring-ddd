package com.springddd.infrastructure.cache;

import com.springddd.infrastructure.cache.util.CacheDefinition;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class CacheDefinitionTest {

    @Test
    void testCacheDefinitionCreation() {
        CacheDefinition definition = new CacheDefinition("user:%s", Duration.ofMinutes(5));

        assertEquals("user:%s", definition.pattern());
        assertEquals(Duration.ofMinutes(5), definition.ttl());
    }

    @Test
    void testBuildKeyWithSingleArg() {
        CacheDefinition definition = CacheDefinition.of("user:%s");
        String key = definition.buildKey("123");

        assertEquals("user:123", key);
    }

    @Test
    void testBuildKeyWithMultipleArgs() {
        CacheDefinition definition = CacheDefinition.of("user:%s:role:%s");
        String key = definition.buildKey("123", "admin");

        assertEquals("user:123:role:admin", key);
    }

    @Test
    void testBuildKeyWithNoArgs() {
        CacheDefinition definition = CacheDefinition.of("all-users");
        String key = definition.buildKey();

        assertEquals("all-users", key);
    }

    @Test
    void testBuildKeyWithDifferentArgTypes() {
        CacheDefinition definition = CacheDefinition.of("user:%s:age:%d");
        String key = definition.buildKey("john", 25);

        assertEquals("user:john:age:25", key);
    }

    @Test
    void testOfWithPatternOnly() {
        CacheDefinition definition = CacheDefinition.of("cache:key");

        assertEquals("cache:key", definition.pattern());
        assertNull(definition.ttl());
    }

    @Test
    void testOfWithPatternAndTtl() {
        CacheDefinition definition = CacheDefinition.of("cache:key", Duration.ofHours(1));

        assertEquals("cache:key", definition.pattern());
        assertEquals(Duration.ofHours(1), definition.ttl());
    }

    @Test
    void testTtlWithVariousDurations() {
        CacheDefinition secondsDef = CacheDefinition.of("s", Duration.ofSeconds(30));
        CacheDefinition minutesDef = CacheDefinition.of("m", Duration.ofMinutes(10));
        CacheDefinition hoursDef = CacheDefinition.of("h", Duration.ofHours(2));

        assertEquals(Duration.ofSeconds(30), secondsDef.ttl());
        assertEquals(Duration.ofMinutes(10), minutesDef.ttl());
        assertEquals(Duration.ofHours(2), hoursDef.ttl());
    }

    @Test
    void testBuildKeyWithComplexPattern() {
        CacheDefinition definition = CacheDefinition.of("dept:%s:menu:%s:children");
        String key = definition.buildKey("100", "200");

        assertEquals("dept:100:menu:200:children", key);
    }

    @Test
    void testRecordEqualsAndHashCode() {
        CacheDefinition def1 = CacheDefinition.of("key", Duration.ofMinutes(5));
        CacheDefinition def2 = CacheDefinition.of("key", Duration.ofMinutes(5));
        CacheDefinition def3 = CacheDefinition.of("key", Duration.ofMinutes(10));

        assertEquals(def1, def2);
        assertNotEquals(def1, def3);
        assertEquals(def1.hashCode(), def2.hashCode());
    }

    @Test
    void testRecordToString() {
        CacheDefinition definition = CacheDefinition.of("test:%s", Duration.ofHours(1));
        String str = definition.toString();

        assertTrue(str.contains("pattern=test:%s"));
        assertTrue(str.contains("ttl="));
    }
}
