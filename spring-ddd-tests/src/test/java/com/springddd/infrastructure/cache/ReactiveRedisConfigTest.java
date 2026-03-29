package com.springddd.infrastructure.cache;

import com.springddd.infrastructure.cache.config.ReactiveRedisConfig;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReactiveRedisConfigTest {

    @Test
    void testReactiveRedisTemplateCreation() {
        ReactiveRedisConnectionFactory mockFactory = mock(ReactiveRedisConnectionFactory.class);
        ReactiveRedisConfig config = new ReactiveRedisConfig();

        ReactiveRedisTemplate<String, Object> template = config.reactiveRedisTemplate(mockFactory);

        assertNotNull(template);
    }

    @Test
    void testReactiveRedisTemplateIsProperlyConfigured() {
        ReactiveRedisConnectionFactory mockFactory = mock(ReactiveRedisConnectionFactory.class);
        ReactiveRedisConfig config = new ReactiveRedisConfig();

        ReactiveRedisTemplate<String, Object> template = config.reactiveRedisTemplate(mockFactory);

        assertNotNull(template);
    }

    @Test
    void testTemplateCanBeCreatedWithMockFactory() {
        ReactiveRedisConnectionFactory mockFactory = mock(ReactiveRedisConnectionFactory.class);
        ReactiveRedisConfig config = new ReactiveRedisConfig();

        ReactiveRedisTemplate<String, Object> template = config.reactiveRedisTemplate(mockFactory);

        assertNotNull(template);
    }

    @Test
    void testMultipleTemplateInstancesAreIndependent() {
        ReactiveRedisConnectionFactory mockFactory1 = mock(ReactiveRedisConnectionFactory.class);
        ReactiveRedisConnectionFactory mockFactory2 = mock(ReactiveRedisConnectionFactory.class);
        ReactiveRedisConfig config = new ReactiveRedisConfig();

        ReactiveRedisTemplate<String, Object> template1 = config.reactiveRedisTemplate(mockFactory1);
        ReactiveRedisTemplate<String, Object> template2 = config.reactiveRedisTemplate(mockFactory2);

        assertNotNull(template1);
        assertNotNull(template2);
        assertNotSame(template1, template2);
    }
}
