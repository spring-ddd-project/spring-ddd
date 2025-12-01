package com.springddd.infrastructure.cache.keys;

import com.springddd.infrastructure.cache.util.CacheDefinition;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

class CacheKeysTest {

    @Test
    void shouldHaveCacheKeysClass() {
        assertNotNull(CacheKeys.class);
    }

    @Test
    void shouldHaveUserAllCacheDefinition() {
        assertNotNull(CacheKeys.USER_ALL);
        assertTrue(CacheKeys.USER_ALL instanceof CacheDefinition);
    }

    @Test
    void shouldHaveUserTokenCacheDefinition() {
        assertNotNull(CacheKeys.USER_TOKEN);
        assertTrue(CacheKeys.USER_TOKEN instanceof CacheDefinition);
    }

    @Test
    void shouldHaveUserDetailCacheDefinition() {
        assertNotNull(CacheKeys.USER_DETAIL);
        assertTrue(CacheKeys.USER_DETAIL instanceof CacheDefinition);
    }

    @Test
    void shouldHaveMenuWithPermissionsCacheDefinition() {
        assertNotNull(CacheKeys.MENU_WITH_PERMISSIONS);
        assertTrue(CacheKeys.MENU_WITH_PERMISSIONS instanceof CacheDefinition);
    }

    @Test
    void shouldHaveMenuWithoutPermissionsCacheDefinition() {
        assertNotNull(CacheKeys.MENU_WITHOUT_PERMISSIONS);
        assertTrue(CacheKeys.MENU_WITHOUT_PERMISSIONS instanceof CacheDefinition);
    }

    @Test
    void shouldHaveGenFilesCacheDefinition() {
        assertNotNull(CacheKeys.GEN_FILES);
        assertTrue(CacheKeys.GEN_FILES instanceof CacheDefinition);
    }

    @Test
    void menuCacheDefinitions_shouldHaveSevenDayTtl() {
        assertEquals(Duration.ofDays(7), CacheKeys.MENU_WITH_PERMISSIONS.ttl());
        assertEquals(Duration.ofDays(7), CacheKeys.MENU_WITHOUT_PERMISSIONS.ttl());
    }

    @Test
    void genFilesCacheDefinition_shouldHaveThreeMinuteTtl() {
        assertEquals(Duration.ofMinutes(3), CacheKeys.GEN_FILES.ttl());
    }

    @Test
    void cacheDefinitions_shouldBeBuildable() {
        assertNotNull(CacheKeys.USER_ALL.buildKey("test"));
        assertNotNull(CacheKeys.USER_TOKEN.buildKey("test"));
        assertNotNull(CacheKeys.USER_DETAIL.buildKey("test"));
        assertNotNull(CacheKeys.MENU_WITH_PERMISSIONS.buildKey("test"));
        assertNotNull(CacheKeys.MENU_WITHOUT_PERMISSIONS.buildKey("test"));
        assertNotNull(CacheKeys.GEN_FILES.buildKey("test"));
    }
}
