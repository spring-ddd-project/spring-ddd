package com.springddd.infrastructure.cache.keys;

import com.springddd.infrastructure.cache.util.CacheDefinition;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

class CacheKeysTest {

    @Test
    void shouldHaveUserAllKey() {
        assertNotNull(CacheKeys.USER_ALL);
        assertTrue(CacheKeys.USER_ALL.pattern().contains("user"));
    }

    @Test
    void shouldHaveUserTokenKey() {
        assertNotNull(CacheKeys.USER_TOKEN);
        assertTrue(CacheKeys.USER_TOKEN.pattern().contains("token"));
    }

    @Test
    void shouldHaveUserDetailKey() {
        assertNotNull(CacheKeys.USER_DETAIL);
        assertTrue(CacheKeys.USER_DETAIL.pattern().contains("detail"));
    }

    @Test
    void shouldHaveMenuWithPermissionsKey() {
        assertNotNull(CacheKeys.MENU_WITH_PERMISSIONS);
        assertNotNull(CacheKeys.MENU_WITH_PERMISSIONS.ttl());
        assertEquals(Duration.ofDays(7), CacheKeys.MENU_WITH_PERMISSIONS.ttl());
    }

    @Test
    void shouldHaveMenuWithoutPermissionsKey() {
        assertNotNull(CacheKeys.MENU_WITHOUT_PERMISSIONS);
        assertNotNull(CacheKeys.MENU_WITHOUT_PERMISSIONS.ttl());
        assertEquals(Duration.ofDays(7), CacheKeys.MENU_WITHOUT_PERMISSIONS.ttl());
    }

    @Test
    void shouldHaveGenFilesKey() {
        assertNotNull(CacheKeys.GEN_FILES);
        assertNotNull(CacheKeys.GEN_FILES.ttl());
        assertEquals(Duration.ofMinutes(3), CacheKeys.GEN_FILES.ttl());
    }
}
