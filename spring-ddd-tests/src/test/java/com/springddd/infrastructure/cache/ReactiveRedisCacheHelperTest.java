package com.springddd.infrastructure.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("ReactiveRedisCacheHelper Tests")
class ReactiveRedisCacheHelperTest {

    @Mock
    private ReactiveRedisTemplate<String, String> redisTemplate;

    @Mock
    private ReactiveValueOperations<String, String> valueOperations;

    private ObjectMapper objectMapper;

    private ReactiveRedisCacheHelper cacheHelper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        cacheHelper = new ReactiveRedisCacheHelper(redisTemplate, objectMapper);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("getCache() should return cached value")
    void getCache_WhenValueExists_ReturnsValue() throws Exception {
        TestData data = new TestData("test", 123);
        String json = objectMapper.writeValueAsString(data);
        when(valueOperations.get("testKey")).thenReturn(Mono.just(json));

        StepVerifier.create(cacheHelper.getCache("testKey", TestData.class))
                .assertNext(result -> {
                    assertThat(result.getName()).isEqualTo("test");
                    assertThat(result.getValue()).isEqualTo(123);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("getCache() should return empty when key not found")
    void getCache_WhenKeyNotFound_ReturnsEmpty() {
        when(valueOperations.get("nonexistent")).thenReturn(Mono.empty());

        StepVerifier.create(cacheHelper.getCache("nonexistent", TestData.class))
                .verifyComplete();
    }

    @Test
    @DisplayName("getOrLoad() should return cached value when exists")
    void getOrLoad_WhenCacheHit_ReturnsCachedValue() throws Exception {
        TestData data = new TestData("cached", 999);
        String json = objectMapper.writeValueAsString(data);
        when(valueOperations.get("cacheKey")).thenReturn(Mono.just(json));

        StepVerifier.create(cacheHelper.getOrLoad("cacheKey", TestData.class, Duration.ofMinutes(5), () -> Mono.just(new TestData("db", 111))))
                .assertNext(result -> {
                    assertThat(result.getName()).isEqualTo("cached");
                    assertThat(result.getValue()).isEqualTo(999);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("getOrLoad() should load from db and cache when cache miss")
    void getOrLoad_WhenCacheMiss_LoadsFromDbAndCaches() {
        TestData dbData = new TestData("fromDb", 222);
        when(valueOperations.get("dbKey")).thenReturn(Mono.empty());
        when(valueOperations.set(eq("dbKey"), anyString(), eq(Duration.ofMinutes(5)))).thenReturn(Mono.just(true));

        StepVerifier.create(cacheHelper.getOrLoad("dbKey", TestData.class, Duration.ofMinutes(5), () -> Mono.just(dbData)))
                .assertNext(result -> {
                    assertThat(result.getName()).isEqualTo("fromDb");
                    assertThat(result.getValue()).isEqualTo(222);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("getOrLoad() should store NULL_MARK for null values and return empty")
    void getOrLoad_WhenDbReturnsNull_StoresNullMarkAndReturnsEmpty() {
        when(valueOperations.get("nullKey")).thenReturn(Mono.empty());
        when(valueOperations.set(eq("nullKey"), eq("__NULL__"), eq(Duration.ofMinutes(1)))).thenReturn(Mono.just(true));

        StepVerifier.create(cacheHelper.getOrLoad("nullKey", TestData.class, Duration.ofMinutes(5), () -> Mono.empty()))
                .verifyComplete();
    }

    @Test
    @DisplayName("setCache() should serialize and store value with TTL")
    void setCache_StoresSerializedValue() throws Exception {
        TestData data = new TestData("toCache", 333);
        when(valueOperations.set(eq("setKey"), anyString(), eq(Duration.ofMinutes(10)))).thenReturn(Mono.just(true));

        StepVerifier.create(cacheHelper.setCache("setKey", data, Duration.ofMinutes(10)))
                .assertNext(result -> assertThat(result).isTrue())
                .verifyComplete();
    }

    @Test
    @DisplayName("deleteCache() should delete matching keys")
    void deleteCache_DeletesMatchingKeys() {
        when(redisTemplate.scan()).thenReturn(Flux.just("deleteKey1", "deleteKey2", "otherKey"));
        when(redisTemplate.delete("deleteKey1")).thenReturn(Mono.just(1L));
        when(redisTemplate.delete("deleteKey2")).thenReturn(Mono.just(1L));

        StepVerifier.create(cacheHelper.deleteCache("deleteKey*"))
                .verifyComplete();
    }

    @Test
    @DisplayName("isAllowed() should return true for first request within limit")
    void isAllowed_FirstRequest_ReturnsTrue() {
        when(valueOperations.increment("rate:test")).thenReturn(Mono.just(1L));
        when(redisTemplate.expire(eq("rate:test"), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(cacheHelper.isAllowed("test", 10, Duration.ofMinutes(1)))
                .assertNext(result -> assertThat(result).isTrue())
                .verifyComplete();
    }

    @Test
    @DisplayName("isAllowed() should return true when count within limit")
    void isAllowed_CountWithinLimit_ReturnsTrue() {
        when(valueOperations.increment("rate:test")).thenReturn(Mono.just(5L));

        StepVerifier.create(cacheHelper.isAllowed("test", 10, Duration.ofMinutes(1)))
                .assertNext(result -> assertThat(result).isTrue())
                .verifyComplete();
    }

    @Test
    @DisplayName("isAllowed() should return false when count exceeds limit")
    void isAllowed_CountExceedsLimit_ReturnsFalse() {
        when(valueOperations.increment("rate:test")).thenReturn(Mono.just(15L));

        StepVerifier.create(cacheHelper.isAllowed("test", 10, Duration.ofMinutes(1)))
                .assertNext(result -> assertThat(result).isFalse())
                .verifyComplete();
    }

    @Test
    @DisplayName("getOrLoadBatch() should load multiple keys")
    void getOrLoadBatch_LoadsMultipleKeys() {
        TestData data1 = new TestData("batch1", 1);
        TestData data2 = new TestData("batch2", 2);

        when(valueOperations.get("batchKey1")).thenReturn(Mono.empty());
        when(valueOperations.get("batchKey2")).thenReturn(Mono.just("{\"name\":\"batch2\",\"value\":2}"));
        when(valueOperations.set(eq("batchKey1"), anyString(), any(Duration.class))).thenReturn(Mono.just(true));

        List<String> keys = Arrays.asList("batchKey1", "batchKey2");

        StepVerifier.create(cacheHelper.getOrLoadBatch(keys, key -> {
            if (key.equals("batchKey1")) return Mono.just(data1);
            return Mono.just(data2);
        }, TestData.class, Duration.ofMinutes(5)))
                .assertNext(result -> {
                    assertThat(result.getName()).isEqualTo("batch1");
                    assertThat(result.getValue()).isEqualTo(1);
                })
                .assertNext(result -> {
                    assertThat(result.getName()).isEqualTo("batch2");
                    assertThat(result.getValue()).isEqualTo(2);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("getOrLoad() should fall back to db on error")
    void getOrLoad_OnRedisError_FallsBackToDb() {
        TestData dbData = new TestData("fallback", 444);
        when(valueOperations.get("errorKey")).thenReturn(Mono.error(new RuntimeException("Redis error")));

        StepVerifier.create(cacheHelper.getOrLoad("errorKey", TestData.class, Duration.ofMinutes(5), () -> Mono.just(dbData)))
                .assertNext(result -> {
                    assertThat(result.getName()).isEqualTo("fallback");
                    assertThat(result.getValue()).isEqualTo(444);
                })
                .verifyComplete();
    }

    static class TestData {
        private String name;
        private int value;

        public TestData() {}

        public TestData(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getValue() { return value; }
        public void setValue(int value) { this.value = value; }
    }
}
