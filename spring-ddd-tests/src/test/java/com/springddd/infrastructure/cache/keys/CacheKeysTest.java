package com.springddd.infrastructure.cache.keys;

import com.springddd.infrastructure.cache.util.CacheDefinition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class CacheKeysTest {

    @Test
    @DisplayName("USER_ALL 应生成正确的缓存键模式")
    void userAll_shouldGenerateCorrectPattern() {
        String key = CacheKeys.USER_ALL.buildKey("123");
        assertThat(key).isEqualTo("user:123:*");
    }

    @Test
    @DisplayName("USER_TOKEN 应生成正确的缓存键")
    void userToken_shouldGenerateCorrectKey() {
        String key = CacheKeys.USER_TOKEN.buildKey("456");
        assertThat(key).isEqualTo("user:456:token");
    }

    @Test
    @DisplayName("USER_DETAIL 应生成正确的缓存键")
    void userDetail_shouldGenerateCorrectKey() {
        String key = CacheKeys.USER_DETAIL.buildKey("789");
        assertThat(key).isEqualTo("user:789:detail");
    }

    @Test
    @DisplayName("MENU_WITH_PERMISSIONS 应生成正确的缓存键并带有 TTL")
    void menuWithPermissions_shouldGenerateCorrectKeyWithTtl() {
        String key = CacheKeys.MENU_WITH_PERMISSIONS.buildKey("100");
        assertThat(key).isEqualTo("user:100:menuWithPermissions");
        assertThat(CacheKeys.MENU_WITH_PERMISSIONS.ttl()).isEqualTo(Duration.ofDays(7));
    }

    @Test
    @DisplayName("MENU_WITHOUT_PERMISSIONS 应生成正确的缓存键并带有 TTL")
    void menuWithoutPermissions_shouldGenerateCorrectKeyWithTtl() {
        String key = CacheKeys.MENU_WITHOUT_PERMISSIONS.buildKey("200");
        assertThat(key).isEqualTo("user:200:menuWithoutPermissions");
        assertThat(CacheKeys.MENU_WITHOUT_PERMISSIONS.ttl()).isEqualTo(Duration.ofDays(7));
    }

    @Test
    @DisplayName("GEN_FILES 应生成正确的缓存键并带有 TTL")
    void genFiles_shouldGenerateCorrectKeyWithTtl() {
        String key = CacheKeys.GEN_FILES.buildKey("300");
        assertThat(key).isEqualTo("user:300:files");
        assertThat(CacheKeys.GEN_FILES.ttl()).isEqualTo(Duration.ofMinutes(3));
    }

    @Test
    @DisplayName("USER_ALL 应无 TTL")
    void userAll_shouldHaveNoTtl() {
        assertThat(CacheKeys.USER_ALL.ttl()).isNull();
    }

    @Test
    @DisplayName("CacheDefinition of 无 TTL 应创建 ttl 为 null 的定义")
    void cacheDefinition_ofWithoutTtl_shouldCreateDefinitionWithNullTtl() {
        CacheDefinition def = CacheDefinition.of("test:%s");
        assertThat(def.pattern()).isEqualTo("test:%s");
        assertThat(def.ttl()).isNull();
    }

    @Test
    @DisplayName("CacheDefinition of 有 TTL 应创建带 ttl 的定义")
    void cacheDefinition_ofWithTtl_shouldCreateDefinitionWithTtl() {
        CacheDefinition def = CacheDefinition.of("test:%s", Duration.ofHours(1));
        assertThat(def.pattern()).isEqualTo("test:%s");
        assertThat(def.ttl()).isEqualTo(Duration.ofHours(1));
    }
}
