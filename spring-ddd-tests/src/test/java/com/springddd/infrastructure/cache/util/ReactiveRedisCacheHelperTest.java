package com.springddd.infrastructure.cache.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReactiveRedisCacheHelperTest {

    @Mock
    private ReactiveRedisTemplate<String, String> redisTemplate;

    @Mock
    private ReactiveValueOperations<String, String> valueOps;

    @InjectMocks
    private ReactiveRedisCacheHelper helper;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        helper = new ReactiveRedisCacheHelper(redisTemplate, objectMapper);
    }

    @Test
    @DisplayName("getOrLoad 当缓存命中时应返回缓存值")
    void getOrLoad_whenCacheHit_shouldReturnCached() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get("key")).thenReturn(Mono.just("\"cached\""));

        StepVerifier.create(helper.getOrLoad("key", String.class, Duration.ofMinutes(5), () -> Mono.just("db")))
                .assertNext(result -> assertThat(result).isEqualTo("cached"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getOrLoad 当缓存为空字符串时应从 DB 加载")
    void getOrLoad_whenCacheBlank_shouldLoadFromDb() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get("key")).thenReturn(Mono.just(""));

        StepVerifier.create(helper.getOrLoad("key", String.class, Duration.ofMinutes(5), () -> Mono.just("db")))
                .assertNext(result -> assertThat(result).isEqualTo("db"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getOrLoad 当缓存未命中时应从 DB 加载并写入缓存")
    void getOrLoad_whenCacheMiss_shouldLoadAndWrite() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get("key")).thenReturn(Mono.empty());
        when(valueOps.set(anyString(), anyString(), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(helper.getOrLoad("key", String.class, Duration.ofMinutes(5), () -> Mono.just("db")))
                .assertNext(result -> assertThat(result).isEqualTo("db"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getOrLoad 当缓存包含 NULL_MARK 时应从 DB 加载")
    void getOrLoad_whenCacheContainsNullMark_shouldLoadFromDb() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get("key")).thenReturn(Mono.just("__NULL__"));
        when(valueOps.set(anyString(), anyString(), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(helper.getOrLoad("key", String.class, Duration.ofMinutes(5), () -> Mono.just("db")))
                .assertNext(result -> assertThat(result).isEqualTo("db"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getOrLoad 当 Redis 读取抛出异常时应回退到 DB 加载")
    void getOrLoad_whenRedisThrows_shouldFallbackToDbLoader() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get("key")).thenReturn(Mono.error(new RuntimeException("redis down")));

        StepVerifier.create(helper.getOrLoad("key", String.class, Duration.ofMinutes(5), () -> Mono.just("db")))
                .assertNext(result -> assertThat(result).isEqualTo("db"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getOrLoad 当反序列化缓存值失败时应回退到 DB 加载")
    void getOrLoad_whenDeserializeThrows_shouldFallbackToDbLoader() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get("key")).thenReturn(Mono.just("invalid-json"));

        StepVerifier.create(helper.getOrLoad("key", String.class, Duration.ofMinutes(5), () -> Mono.just("db")))
                .assertNext(result -> assertThat(result).isEqualTo("db"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getOrLoad 当序列化失败时应回退到 DB 加载")
    void getOrLoad_whenSerializeThrows_shouldFallbackToDbLoader() throws Exception {
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        when(mockMapper.writeValueAsString(any())).thenThrow(new RuntimeException("serialize fail"));
        ReactiveRedisCacheHelper helperWithMockMapper = new ReactiveRedisCacheHelper(redisTemplate, mockMapper);

        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get("key")).thenReturn(Mono.empty());

        StepVerifier.create(helperWithMockMapper.getOrLoad("key", String.class, Duration.ofMinutes(5), () -> Mono.just("db")))
                .assertNext(result -> assertThat(result).isEqualTo("db"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getOrLoad 当缓存写入失败时应回退到 DB 加载")
    void getOrLoad_whenCacheSetFails_shouldFallbackToDbLoader() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get("key")).thenReturn(Mono.empty());

        java.util.concurrent.atomic.AtomicInteger setCount = new java.util.concurrent.atomic.AtomicInteger(0);
        when(valueOps.set(anyString(), anyString(), any(Duration.class))).thenAnswer(inv -> {
            if (setCount.incrementAndGet() == 1) {
                return Mono.error(new RuntimeException("set fail"));
            }
            return Mono.just(true);
        });

        java.util.concurrent.atomic.AtomicInteger loaderCount = new java.util.concurrent.atomic.AtomicInteger(0);
        java.util.function.Supplier<Mono<String>> dbLoader = () -> {
            int count = loaderCount.incrementAndGet();
            return Mono.just(count == 1 ? "db" : "fallback");
        };

        StepVerifier.create(helper.getOrLoad("key", String.class, Duration.ofMinutes(5), dbLoader))
                .assertNext(result -> assertThat(result).isEqualTo("fallback"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getOrLoadBatch 当部分缓存命中时应返回缓存值和 DB 值的混合结果")
    void getOrLoadBatch_whenPartialHit_shouldReturnMixedResults() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get("key1")).thenReturn(Mono.just("\"hit1\""));
        when(valueOps.get("key2")).thenReturn(Mono.empty());
        when(valueOps.set(anyString(), anyString(), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(
                        helper.getOrLoadBatch(
                                        List.of("key1", "key2"),
                                        key -> Mono.just("db" + key),
                                        String.class,
                                        Duration.ofMinutes(5))
                                .collectList())
                .assertNext(results -> assertThat(results).containsExactlyInAnyOrder("hit1", "dbkey2"))
                .verifyComplete();
    }

    @Test
    @DisplayName("setCache 应写入缓存")
    void setCache_shouldWriteToCache() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.set(anyString(), anyString(), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(helper.setCache("key", "value", Duration.ofMinutes(5)))
                .assertNext(result -> assertThat(result).isTrue())
                .verifyComplete();
    }

    @Test
    @DisplayName("setCache 当值为 null 时应存储 NULL_MARK")
    void setCache_whenValueIsNull_shouldStoreNullMark() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.set("key", "__NULL__", Duration.ofMinutes(5))).thenReturn(Mono.just(true));

        StepVerifier.create(helper.setCache("key", null, Duration.ofMinutes(5)))
                .assertNext(result -> assertThat(result).isTrue())
                .verifyComplete();
    }

    @Test
    @DisplayName("setCache 当序列化失败时应返回错误")
    void setCache_whenSerializeThrows_shouldReturnError() throws Exception {
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        when(mockMapper.writeValueAsString(any())).thenThrow(new RuntimeException("serialize fail"));
        ReactiveRedisCacheHelper helperWithMockMapper = new ReactiveRedisCacheHelper(redisTemplate, mockMapper);

        StepVerifier.create(helperWithMockMapper.setCache("key", "value", Duration.ofMinutes(5)))
                .expectError()
                .verify();
    }

    @Test
    @DisplayName("getCache 应读取缓存")
    void getCache_shouldReadFromCache() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get("key")).thenReturn(Mono.just("\"value\""));

        StepVerifier.create(helper.getCache("key", String.class))
                .assertNext(result -> assertThat(result).isEqualTo("value"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getCache 当缓存值为无效 JSON 时应返回错误")
    void getCache_whenInvalidJson_shouldReturnError() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get("key")).thenReturn(Mono.just("invalid-json"));

        StepVerifier.create(helper.getCache("key", String.class))
                .expectError()
                .verify();
    }

    @Test
    @DisplayName("deleteCache 当 key 不含通配符时应直接删除")
    void deleteCache_whenNoWildcard_shouldDeleteDirectly() {
        when(redisTemplate.delete("key")).thenReturn(Mono.just(1L));

        StepVerifier.create(helper.deleteCache("key"))
                .verifyComplete();

        verify(redisTemplate).delete("key");
    }

    @Test
    @DisplayName("deleteCache 当 key 包含 * 通配符时应 SCAN 并删除匹配键")
    void deleteCache_withStarPattern_shouldScanAndDeleteMatchingKeys() {
        when(redisTemplate.scan()).thenReturn(Flux.just("user:1", "user:2", "order:1"));
        when(redisTemplate.delete("user:1")).thenReturn(Mono.just(1L));
        when(redisTemplate.delete("user:2")).thenReturn(Mono.just(1L));

        StepVerifier.create(helper.deleteCache("user:*"))
                .verifyComplete();

        verify(redisTemplate).scan();
        verify(redisTemplate).delete("user:1");
        verify(redisTemplate).delete("user:2");
        verify(redisTemplate, never()).delete("order:1");
    }

    @Test
    @DisplayName("deleteCache 当 key 包含 ? 通配符时应匹配单个字符")
    void deleteCache_withQuestionMarkPattern_shouldMatchSingleChar() {
        when(redisTemplate.scan()).thenReturn(Flux.just("abc", "abbc", "ac"));
        when(redisTemplate.delete("abc")).thenReturn(Mono.just(1L));

        StepVerifier.create(helper.deleteCache("a?c*"))
                .verifyComplete();

        verify(redisTemplate).delete("abc");
        verify(redisTemplate, never()).delete("abbc");
        verify(redisTemplate, never()).delete("ac");
    }

    @Test
    @DisplayName("isAllowed 当计数在限制内时应返回 true")
    void isAllowed_whenUnderLimit_shouldReturnTrue() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.increment("rate:key")).thenReturn(Mono.just(1L));
        when(redisTemplate.expire("rate:key", Duration.ofMinutes(1))).thenReturn(Mono.just(true));

        StepVerifier.create(helper.isAllowed("key", 5, Duration.ofMinutes(1)))
                .assertNext(result -> assertThat(result).isTrue())
                .verifyComplete();
    }

    @Test
    @DisplayName("isAllowed 当计数在限制内但不是第一次时应返回 true")
    void isAllowed_whenCountWithinLimitButNotFirst_shouldReturnTrue() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.increment("rate:key")).thenReturn(Mono.just(3L));

        StepVerifier.create(helper.isAllowed("key", 5, Duration.ofMinutes(1)))
                .assertNext(result -> assertThat(result).isTrue())
                .verifyComplete();
    }

    @Test
    @DisplayName("isAllowed 当计数超过限制时应返回 false")
    void isAllowed_whenOverLimit_shouldReturnFalse() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.increment("rate:key")).thenReturn(Mono.just(10L));

        StepVerifier.create(helper.isAllowed("key", 5, Duration.ofMinutes(1)))
                .assertNext(result -> assertThat(result).isFalse())
                .verifyComplete();
    }

    @Test
    @DisplayName("deserialize 当 json 为 null 时应返回空 Mono")
    void deserialize_whenJsonIsNull_shouldReturnEmpty() throws Exception {
        Method deserializeMethod = ReactiveRedisCacheHelper.class.getDeclaredMethod("deserialize", String.class, Class.class);
        deserializeMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        Mono<String> result = (Mono<String>) deserializeMethod.invoke(helper, (String) null, String.class);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
