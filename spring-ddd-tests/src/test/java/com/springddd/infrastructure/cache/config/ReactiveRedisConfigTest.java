package com.springddd.infrastructure.cache.config;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class ReactiveRedisConfigTest {

    @Test
    void reactiveRedisTemplate_shouldCreateTemplate() {
        ReactiveRedisConnectionFactory factory = mock(ReactiveRedisConnectionFactory.class);
        ReactiveRedisConfig config = new ReactiveRedisConfig();
        ReactiveRedisTemplate<String, Object> template = config.reactiveRedisTemplate(factory);
        assertNotNull(template);
    }
}
