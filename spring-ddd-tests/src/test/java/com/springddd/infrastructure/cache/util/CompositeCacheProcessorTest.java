package com.springddd.infrastructure.cache.util;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class CompositeCacheProcessorTest {

    @Test
    void testGetOrLoadFirstHit() {
        CompositeCacheProcessor composite = new CompositeCacheProcessor();
        composite.addProcessor(new TestCacheProcessor() {
            @Override
            public <R> Mono<R> getOrLoad(String key, Class<?> clazz, Duration ttl, java.util.function.Supplier<Mono<R>> dbLoader) {
                return Mono.just((R) "cached");
            }
        });
        composite.addProcessor(new TestCacheProcessor());

        StepVerifier.create(composite.getOrLoad("key", String.class, Duration.ofMinutes(1), () -> Mono.just("db")))
                .expectNext("cached")
                .verifyComplete();
    }

    @Test
    void testGetOrLoadFallback() {
        CompositeCacheProcessor composite = new CompositeCacheProcessor();
        composite.addProcessor(new TestCacheProcessor()); // empty
        composite.addProcessor(new TestCacheProcessor() {
            @Override
            public <R> Mono<R> getOrLoad(String key, Class<?> clazz, Duration ttl, java.util.function.Supplier<Mono<R>> dbLoader) {
                return Mono.just((R) "fallback");
            }
        });

        StepVerifier.create(composite.getOrLoad("key", String.class, Duration.ofMinutes(1), () -> Mono.just("db")))
                .expectNext("fallback")
                .verifyComplete();
    }

    @Test
    void testSetCache() {
        CompositeCacheProcessor composite = new CompositeCacheProcessor();
        composite.addProcessor(new TestCacheProcessor() {
            @Override
            public <T> Mono<Boolean> setCache(String key, T value, Duration ttl) {
                return Mono.just(true);
            }
        });

        StepVerifier.create(composite.setCache("key", "value", Duration.ofMinutes(1)))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testGetCache() {
        CompositeCacheProcessor composite = new CompositeCacheProcessor();
        composite.addProcessor(new TestCacheProcessor() {
            @Override
            public <T> Mono<T> getCache(String key, Class<T> clazz) {
                return Mono.just((T) "cached");
            }
        });

        StepVerifier.create(composite.getCache("key", String.class))
                .expectNext("cached")
                .verifyComplete();
    }

    @Test
    void testDeleteCache() {
        CompositeCacheProcessor composite = new CompositeCacheProcessor();
        composite.addProcessor(new TestCacheProcessor() {
            @Override
            public Mono<Void> deleteCache(String key) {
                return Mono.empty();
            }
        });

        StepVerifier.create(composite.deleteCache("key"))
                .verifyComplete();
    }

    static class TestCacheProcessor implements CacheProcessor {
        @Override
        public <R> Mono<R> getOrLoad(String key, Class<?> clazz, Duration ttl, java.util.function.Supplier<Mono<R>> dbLoader) {
            return Mono.empty();
        }

        @Override
        public <T> Mono<Boolean> setCache(String key, T value, Duration ttl) {
            return Mono.empty();
        }

        @Override
        public <T> Mono<T> getCache(String key, Class<T> clazz) {
            return Mono.empty();
        }

        @Override
        public Mono<Void> deleteCache(String key) {
            return Mono.empty();
        }
    }
}
