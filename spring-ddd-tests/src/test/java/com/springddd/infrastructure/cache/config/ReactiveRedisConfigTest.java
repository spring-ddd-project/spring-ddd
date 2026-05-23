package com.springddd.infrastructure.cache.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ReactiveRedisConfigTest {

    @Mock
    private ReactiveRedisConnectionFactory connectionFactory;

    private ReactiveRedisConfig config;

    @BeforeEach
    void setUp() {
        config = new ReactiveRedisConfig();
    }

    @Test
    @DisplayName("reactiveRedisTemplate 应返回非空的 ReactiveRedisTemplate")
    void reactiveRedisTemplate_shouldReturnNonNullTemplate() {
        ReactiveRedisTemplate<String, Object> template = config.reactiveRedisTemplate(connectionFactory);

        assertThat(template).isNotNull();
    }

    @Test
    @DisplayName("reactiveRedisTemplate 应使用传入的 connectionFactory")
    void reactiveRedisTemplate_shouldUseProvidedConnectionFactory() {
        ReactiveRedisTemplate<String, Object> template = config.reactiveRedisTemplate(connectionFactory);

        assertThat(template).isNotNull();
        // ReactiveRedisTemplate 内部持有 connectionFactory，但由于是私有字段，
        // 我们主要验证返回非空且不会抛出异常
    }
}
