package com.springddd.infrastructure.cache;

import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.CacheDefinition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CacheKeys Tests")
class CacheKeysTest {

    @Test
    @DisplayName("USER_ALL should build key with correct pattern")
    void userAll_BuildsCorrectPattern() {
        String key = CacheKeys.USER_ALL.buildKey("user123");
        assertThat(key).isEqualTo("user:user123:*");
    }

    @Test
    @DisplayName("USER_TOKEN should build key with correct pattern")
    void userToken_BuildsCorrectPattern() {
        String key = CacheKeys.USER_TOKEN.buildKey("user123");
        assertThat(key).isEqualTo("user:user123:token");
    }

    @Test
    @DisplayName("USER_DETAIL should build key with correct pattern")
    void userDetail_BuildsCorrectPattern() {
        String key = CacheKeys.USER_DETAIL.buildKey("user123");
        assertThat(key).isEqualTo("user:user123:detail");
    }

    @Test
    @DisplayName("MENU_WITH_PERMISSIONS should build key with correct pattern and TTL")
    void menuWithPermissions_BuildsCorrectPatternAndTtl() {
        String key = CacheKeys.MENU_WITH_PERMISSIONS.buildKey("user123");
        assertThat(key).isEqualTo("user:user123:menuWithPermissions");
        assertThat(CacheKeys.MENU_WITH_PERMISSIONS.ttl()).isEqualTo(Duration.ofDays(7));
    }

    @Test
    @DisplayName("MENU_WITHOUT_PERMISSIONS should build key with correct pattern and TTL")
    void menuWithoutPermissions_BuildsCorrectPatternAndTtl() {
        String key = CacheKeys.MENU_WITHOUT_PERMISSIONS.buildKey("user123");
        assertThat(key).isEqualTo("user:user123:menuWithoutPermissions");
        assertThat(CacheKeys.MENU_WITHOUT_PERMISSIONS.ttl()).isEqualTo(Duration.ofDays(7));
    }

    @Test
    @DisplayName("GEN_FILES should build key with correct pattern and TTL")
    void genFiles_BuildsCorrectPatternAndTtl() {
        String key = CacheKeys.GEN_FILES.buildKey("user123");
        assertThat(key).isEqualTo("user:user123:files");
        assertThat(CacheKeys.GEN_FILES.ttl()).isEqualTo(Duration.ofMinutes(3));
    }

    @Test
    @DisplayName("CacheDefinition.of without TTL should create definition with null TTL")
    void cacheDefinitionOf_WithoutTtl_HasNullTtl() {
        CacheDefinition definition = CacheKeys.USER_ALL;
        assertThat(definition.pattern()).isNotNull();
        assertThat(definition.ttl()).isNull();
    }

    @Test
    @DisplayName("CacheDefinition.of with TTL should create definition with correct TTL")
    void cacheDefinitionOf_WithTtl_HasCorrectTtl() {
        CacheDefinition definition = CacheKeys.MENU_WITH_PERMISSIONS;
        assertThat(definition.pattern()).isNotNull();
        assertThat(definition.ttl()).isEqualTo(Duration.ofDays(7));
    }

    @Test
    @DisplayName("Multiple args should be substituted correctly")
    void multipleArgs_AreSubstitutedCorrectly() {
        CacheDefinition customDefinition = CacheDefinition.of("gen:%s:%s:files");
        String key = customDefinition.buildKey("project1", "module1");
        assertThat(key).isEqualTo("gen:project1:module1:files");
    }
}
