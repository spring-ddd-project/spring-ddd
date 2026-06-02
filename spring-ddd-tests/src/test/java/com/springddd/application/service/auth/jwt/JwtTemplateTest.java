package com.springddd.application.service.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtTemplateTest {

    private JwtTemplate jwtTemplate;

    @BeforeEach
    void setUp() {
        JwtSecret jwtSecret = new JwtSecret();
        jwtSecret.setKey("mySecretKey123456789012345678901234");
        jwtSecret.setTtl(7);
        jwtTemplate = new JwtTemplate(jwtSecret);
    }

    @Test
    void generateToken_shouldReturnNonNullToken() {
        String token = jwtTemplate.generateToken(Map.of("userId", 1L));
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void parseToken_shouldReturnCorrectClaims() {
        String token = jwtTemplate.generateToken(Map.of("userId", 1L));
        Jws<Claims> jws = jwtTemplate.parseToken(token);
        assertNotNull(jws);
        assertNotNull(jws.getPayload());
        assertEquals(1L, jws.getPayload().get("userId", Long.class));
    }
}
