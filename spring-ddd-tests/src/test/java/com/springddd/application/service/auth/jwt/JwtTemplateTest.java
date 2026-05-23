package com.springddd.application.service.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTemplateTest {

    private JwtTemplate jwtTemplate;

    @BeforeEach
    void setUp() {
        JwtSecret jwtSecret = new JwtSecret();
        jwtSecret.setKey("my-secret-key-my-secret-key-my-secret-key");
        jwtSecret.setTtl(7);
        jwtTemplate = new JwtTemplate(jwtSecret);
    }

    @Test
    @DisplayName("generateToken 应生成非空 token")
    void generateToken_shouldGenerateNonNullToken() {
        String token = jwtTemplate.generateToken(Map.of("userId", 1L));

        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("parseToken 应正确解析 token")
    void parseToken_shouldParseTokenCorrectly() {
        String token = jwtTemplate.generateToken(Map.of("userId", 1L));

        Jws<Claims> claims = jwtTemplate.parseToken(token);

        assertThat(claims.getPayload().get("userId", Long.class)).isEqualTo(1L);
    }
}
