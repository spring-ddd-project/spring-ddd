package com.springddd.infrastructure.cache.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReactiveRedisCacheHelperTest {

    @Mock
    private ReactiveRedisTemplate<String, String> redisTemplate;

    @Mock
    private ReactiveValueOperations<String, String> valueOperations;

    @Mock
    private ObjectMapper objectMapper;

    private ReactiveRedisCacheHelper cacheHelper;

    @BeforeEach
    void setUp() {
        cacheHelper = new ReactiveRedisCacheHelper(redisTemplate, objectMapper);
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    // ========== getOrLoad ==========

    @Test
    void getOrLoad_shouldReturnCachedValue_whenCacheHit() throws Exception {
        String key = "user:1:detail";
        String json = "{\"name\":\"cached\"}";
        TestUser cachedUser = new TestUser("cached");

        when(valueOperations.get(key)).thenReturn(Mono.just(json));
        when(objectMapper.readValue(json, TestUser.class)).thenReturn(cachedUser);

        StepVerifier.create(cacheHelper.getOrLoad(key, TestUser.class, Duration.ofMinutes(5),
                () -> Mono.just(new TestUser("db"))))
                .assertNext(result -> assertEquals("cached", result.name))
                .verifyComplete();
    }

    @Test
    void getOrLoad_shouldLoadFromDb_whenCacheHitNullMark() throws Exception {
        String key = "user:1:detail";
        TestUser dbUser = new TestUser("db");
        String json = "{\"name\":\"db\"}";

        when(valueOperations.get(key)).thenReturn(Mono.just("__NULL__"));
        when(objectMapper.writeValueAsString(dbUser)).thenReturn(json);
        when(valueOperations.set(eq(key), eq(json), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(cacheHelper.getOrLoad(key, TestUser.class, Duration.ofMinutes(5),
                () -> Mono.just(dbUser)))
                .assertNext(result -> assertEquals("db", result.name))
                .verifyComplete();
    }

    @Test
    void getOrLoad_shouldLoadFromDbAndSetCache_whenCacheMiss() throws Exception {
        String key = "user:1:detail";
        TestUser dbUser = new TestUser("db");
        String json = "{\"name\":\"db\"}";

        when(valueOperations.get(key)).thenReturn(Mono.empty());
        when(objectMapper.writeValueAsString(dbUser)).thenReturn(json);
        when(valueOperations.set(eq(key), eq(json), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(cacheHelper.getOrLoad(key, TestUser.class, Duration.ofMinutes(5),
                () -> Mono.just(dbUser)))
                .assertNext(result -> assertEquals("db", result.name))
                .verifyComplete();
    }

    @Test
    void getOrLoad_shouldReturnEmpty_whenDbReturnsEmpty() {
        String key = "user:1:detail";
        when(valueOperations.get(key)).thenReturn(Mono.empty());

        StepVerifier.create(cacheHelper.getOrLoad(key, TestUser.class, Duration.ofMinutes(5),
                () -> Mono.empty()))
                .verifyComplete();
    }

    @Test
    void getOrLoad_shouldSetNullMark_whenDbReturnsNull() {
        String key = "user:1:detail";
        when(valueOperations.get(key)).thenReturn(Mono.empty());
        lenient().when(valueOperations.set(eq(key), eq("__NULL__"), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(cacheHelper.getOrLoad(key, TestUser.class, Duration.ofMinutes(5),
                () -> Mono.create(sink -> sink.success(null))))
                .verifyComplete();
    }

    @Test
    void getOrLoad_shouldFallbackToDb_whenRedisGetError() throws Exception {
        String key = "user:1:detail";
        TestUser dbUser = new TestUser("fallback");

        when(valueOperations.get(key)).thenReturn(Mono.error(new RuntimeException("redis down")));

        StepVerifier.create(cacheHelper.getOrLoad(key, TestUser.class, Duration.ofMinutes(5),
                () -> Mono.just(dbUser)))
                .assertNext(result -> assertEquals("fallback", result.name))
                .verifyComplete();
    }

    @Test
    void getOrLoad_shouldFallbackToDb_whenDeserializeError() throws Exception {
        String key = "user:1:detail";
        String badJson = "invalid";
        TestUser dbUser = new TestUser("fallback");

        when(valueOperations.get(key)).thenReturn(Mono.just(badJson));
        when(objectMapper.readValue(badJson, TestUser.class)).thenThrow(new JsonProcessingException("bad json") {});

        StepVerifier.create(cacheHelper.getOrLoad(key, TestUser.class, Duration.ofMinutes(5),
                () -> Mono.just(dbUser)))
                .assertNext(result -> assertEquals("fallback", result.name))
                .verifyComplete();
    }

    @Test
    void getOrLoad_shouldFallbackToDb_whenSerializeError() throws Exception {
        String key = "user:1:detail";
        TestUser dbUser = new TestUser("db");

        when(valueOperations.get(key)).thenReturn(Mono.empty());
        when(objectMapper.writeValueAsString(dbUser)).thenThrow(new JsonProcessingException("serialize error") {});

        StepVerifier.create(cacheHelper.getOrLoad(key, TestUser.class, Duration.ofMinutes(5),
                () -> Mono.just(dbUser)))
                .assertNext(result -> assertEquals("db", result.name))
                .verifyComplete();
    }

    @Test
    void getOrLoad_shouldFallbackToDb_whenSetCacheFails() throws Exception {
        String key = "user:1:detail";
        TestUser dbUser = new TestUser("db");
        String json = "{\"name\":\"db\"}";

        when(valueOperations.get(key)).thenReturn(Mono.empty());
        when(objectMapper.writeValueAsString(dbUser)).thenReturn(json);
        when(valueOperations.set(eq(key), eq(json), any(Duration.class)))
                .thenReturn(Mono.error(new RuntimeException("redis set error")));

        StepVerifier.create(cacheHelper.getOrLoad(key, TestUser.class, Duration.ofMinutes(5),
                () -> Mono.just(dbUser)))
                .assertNext(result -> assertEquals("db", result.name))
                .verifyComplete();
    }

    @Test
    void getOrLoad_shouldLoadFromDb_whenCacheHitBlankValue() throws Exception {
        String key = "user:1:detail";
        TestUser dbUser = new TestUser("db");
        String json = "{\"name\":\"db\"}";

        when(valueOperations.get(key)).thenReturn(Mono.just("   "));
        when(objectMapper.writeValueAsString(dbUser)).thenReturn(json);
        when(valueOperations.set(eq(key), eq(json), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(cacheHelper.getOrLoad(key, TestUser.class, Duration.ofMinutes(5),
                () -> Mono.just(dbUser)))
                .assertNext(result -> assertEquals("db", result.name))
                .verifyComplete();
    }

    @Test
    void getOrLoad_shouldReturnEmpty_whenDbEmptyAndSetNullMarkFails() {
        String key = "user:1:detail";

        when(valueOperations.get(key)).thenReturn(Mono.empty());

        StepVerifier.create(cacheHelper.getOrLoad(key, TestUser.class, Duration.ofMinutes(5),
                () -> Mono.empty()))
                .verifyComplete();
    }

    // ========== getOrLoadBatch ==========

    @Test
    void getOrLoadBatch_shouldProcessMultipleKeys() throws Exception {
        List<String> keys = Arrays.asList("key1", "key2");
        TestUser user1 = new TestUser("user1");
        TestUser user2 = new TestUser("user2");
        String json1 = "{\"name\":\"user1\"}";
        String json2 = "{\"name\":\"user2\"}";

        when(valueOperations.get("key1")).thenReturn(Mono.just(json1));
        when(valueOperations.get("key2")).thenReturn(Mono.empty());
        when(objectMapper.readValue(json1, TestUser.class)).thenReturn(user1);
        when(objectMapper.writeValueAsString(user2)).thenReturn(json2);
        when(valueOperations.set(eq("key2"), eq(json2), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(cacheHelper.getOrLoadBatch(keys, k -> Mono.just(user2), TestUser.class, Duration.ofMinutes(5)))
                .assertNext(u -> assertEquals("user1", u.name))
                .assertNext(u -> assertEquals("user2", u.name))
                .verifyComplete();
    }

    // ========== isAllowed ==========

    @Test
    void isAllowed_shouldReturnTrueAndSetExpiry_whenFirstRequest() {
        String key = "api:/test";
        String redisKey = "rate:" + key;
        when(valueOperations.increment(redisKey)).thenReturn(Mono.just(1L));
        when(redisTemplate.expire(redisKey, Duration.ofMinutes(1))).thenReturn(Mono.just(true));

        StepVerifier.create(cacheHelper.isAllowed(key, 10, Duration.ofMinutes(1)))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void isAllowed_shouldReturnTrue_whenWithinLimit() {
        String key = "api:/test";
        String redisKey = "rate:" + key;
        when(valueOperations.increment(redisKey)).thenReturn(Mono.just(5L));

        StepVerifier.create(cacheHelper.isAllowed(key, 10, Duration.ofMinutes(1)))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void isAllowed_shouldReturnFalse_whenOverLimit() {
        String key = "api:/test";
        String redisKey = "rate:" + key;
        when(valueOperations.increment(redisKey)).thenReturn(Mono.just(11L));

        StepVerifier.create(cacheHelper.isAllowed(key, 10, Duration.ofMinutes(1)))
                .expectNext(false)
                .verifyComplete();
    }

    // ========== setCache ==========

    @Test
    void setCache_shouldStoreValue() throws Exception {
        String key = "test:key";
        TestUser user = new TestUser("test");
        String json = "{\"name\":\"test\"}";

        when(objectMapper.writeValueAsString(user)).thenReturn(json);
        when(valueOperations.set(eq(key), eq(json), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(cacheHelper.setCache(key, user, Duration.ofMinutes(5)))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void setCache_shouldStoreNullMark_whenValueIsNull() {
        String key = "test:key";
        when(valueOperations.set(eq(key), eq("__NULL__"), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(cacheHelper.setCache(key, null, Duration.ofMinutes(5)))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void setCache_shouldPropagateError_whenSerializeFails() throws Exception {
        String key = "test:key";
        TestUser user = new TestUser("test");

        when(objectMapper.writeValueAsString(user)).thenThrow(new JsonProcessingException("fail") {});

        StepVerifier.create(cacheHelper.setCache(key, user, Duration.ofMinutes(5)))
                .expectError(JsonProcessingException.class)
                .verify();
    }

    // ========== getCache ==========

    @Test
    void getCache_shouldReturnValue_whenExists() throws Exception {
        String key = "test:key";
        String json = "{\"name\":\"test\"}";
        when(valueOperations.get(key)).thenReturn(Mono.just(json));
        when(objectMapper.readValue(json, TestUser.class)).thenReturn(new TestUser("test"));

        StepVerifier.create(cacheHelper.getCache(key, TestUser.class))
                .assertNext(u -> assertEquals("test", u.name))
                .verifyComplete();
    }

    @Test
    void getCache_shouldReturnEmpty_whenRedisReturnsNull() {
        String key = "test:key";
        when(valueOperations.get(key)).thenReturn(Mono.create(sink -> sink.success(null)));

        StepVerifier.create(cacheHelper.getCache(key, TestUser.class))
                .verifyComplete();
    }

    @Test
    void getCache_shouldReturnEmpty_whenNotExists() {
        String key = "test:key";
        when(valueOperations.get(key)).thenReturn(Mono.empty());

        StepVerifier.create(cacheHelper.getCache(key, TestUser.class))
                .verifyComplete();
    }

    @Test
    void getCache_shouldReturnEmpty_whenNullMark() {
        String key = "test:key";
        when(valueOperations.get(key)).thenReturn(Mono.just("__NULL__"));

        StepVerifier.create(cacheHelper.getCache(key, TestUser.class))
                .verifyComplete();
    }

    @Test
    void getCache_shouldReturnEmpty_whenBlankValue() {
        String key = "test:key";
        when(valueOperations.get(key)).thenReturn(Mono.just("   "));

        StepVerifier.create(cacheHelper.getCache(key, TestUser.class))
                .verifyComplete();
    }

    @Test
    void getCache_shouldReturnError_whenDeserializeFails() throws Exception {
        String key = "test:key";
        String badJson = "bad";
        when(valueOperations.get(key)).thenReturn(Mono.just(badJson));
        when(objectMapper.readValue(badJson, TestUser.class)).thenThrow(new JsonProcessingException("bad") {});

        StepVerifier.create(cacheHelper.getCache(key, TestUser.class))
                .expectError(JsonProcessingException.class)
                .verify();
    }

    // ========== deleteCache ==========

    @Test
    void deleteCache_shouldDeleteDirectKey() {
        String key = "test:key";
        when(redisTemplate.delete(key)).thenReturn(Mono.just(1L));

        StepVerifier.create(cacheHelper.deleteCache(key))
                .verifyComplete();
    }

    @Test
    void deleteCache_shouldScanAndDeletePatternKeys_withStar() {
        String pattern = "test:*";
        when(redisTemplate.scan()).thenReturn(Flux.just("test:a", "test:b", "other:c"));
        when(redisTemplate.delete(anyString())).thenReturn(Mono.just(1L));

        StepVerifier.create(cacheHelper.deleteCache(pattern))
                .verifyComplete();

        verify(redisTemplate).delete("test:a");
        verify(redisTemplate).delete("test:b");
        verify(redisTemplate, never()).delete("other:c");
    }

    @Test
    void deleteCache_shouldDeleteDirectKey_withQuestionMark() {
        String key = "test:?";
        when(redisTemplate.delete(key)).thenReturn(Mono.just(1L));

        StepVerifier.create(cacheHelper.deleteCache(key))
                .verifyComplete();
    }

    @Test
    void deleteCache_shouldScanAndDeletePatternKeys_withQuestionMarkInPattern() {
        String pattern = "test:?x*";
        when(redisTemplate.scan()).thenReturn(Flux.just("test:abx", "test:axy", "test:ayy", "other:c"));
        when(redisTemplate.delete(anyString())).thenReturn(Mono.just(1L));

        StepVerifier.create(cacheHelper.deleteCache(pattern))
                .verifyComplete();

        verify(redisTemplate, never()).delete("test:abx");
        verify(redisTemplate).delete("test:axy");
        verify(redisTemplate, never()).delete("test:ayy");
        verify(redisTemplate, never()).delete("other:c");
    }

    // Helper record
    record TestUser(String name) {}

    @Test
    void deserialize_shouldReturnEmpty_whenJsonIsNull() throws Exception {
        java.lang.reflect.Method method = ReactiveRedisCacheHelper.class.getDeclaredMethod("deserialize", String.class, Class.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Mono<TestUser> result = (Mono<TestUser>) method.invoke(cacheHelper, null, TestUser.class);
        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void deserialize_shouldReturnEmpty_whenJsonIsBlank() throws Exception {
        java.lang.reflect.Method method = ReactiveRedisCacheHelper.class.getDeclaredMethod("deserialize", String.class, Class.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Mono<TestUser> result = (Mono<TestUser>) method.invoke(cacheHelper, "   ", TestUser.class);
        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void deserialize_shouldReturnEmpty_whenJsonIsNullMark() throws Exception {
        java.lang.reflect.Method method = ReactiveRedisCacheHelper.class.getDeclaredMethod("deserialize", String.class, Class.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Mono<TestUser> result = (Mono<TestUser>) method.invoke(cacheHelper, "__NULL__", TestUser.class);
        StepVerifier.create(result).verifyComplete();
    }
}
