package com.springddd.application.service.auth;

import com.springddd.application.service.auth.jwt.JwtSecret;
import com.springddd.application.service.auth.jwt.JwtTemplate;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtTemplate Tests")
class JwtTemplateTest {

    @Mock
    private JwtSecret jwtSecret;

    private JwtTemplate jwtTemplate;

    private static final String SECRET_KEY = "this-is-a-very-long-secret-key-for-testing-purposes-at-least-256-bits";
    private static final Integer TTL = 30;

    @BeforeEach
    void setUp() {
        when(jwtSecret.getKey()).thenReturn(SECRET_KEY);
        when(jwtSecret.getTtl()).thenReturn(TTL);
        jwtTemplate = new JwtTemplate(jwtSecret);
    }

    @Test
    @DisplayName("generateToken() should create valid token with claims")
    void generateToken_WithClaims_CreatesValidToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "testuser");
        claims.put("userId", 1L);
        claims.put("roles", "admin,user");

        String token = jwtTemplate.generateToken(claims);

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("generateToken() should create token that can be parsed")
    void generateToken_CreatesTokenThatCanBeParsed() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "testuser");
        claims.put("userId", 1L);

        String token = jwtTemplate.generateToken(claims);
        Jws<Claims> parsedToken = jwtTemplate.parseToken(token);

        assertThat(parsedToken).isNotNull();
        assertThat(parsedToken.getPayload().get("username")).isEqualTo("testuser");
        assertThat(parsedToken.getPayload().get("userId")).isEqualTo(1);
    }

    @Test
    @DisplayName("generateToken() with empty claims should create valid token")
    void generateToken_WithEmptyClaims_CreatesValidToken() {
        Map<String, Object> claims = new HashMap<>();

        String token = jwtTemplate.generateToken(claims);

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    @DisplayName("parseToken() should extract claims from generated token")
    void parseToken_ExtractsClaimsCorrectly() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", "testuser");
        claims.put("userId", 123L);
        claims.put("email", "test@example.com");

        String token = jwtTemplate.generateToken(claims);
        Jws<Claims> parsed = jwtTemplate.parseToken(token);

        assertThat(parsed.getPayload().getSubject()).isEqualTo("testuser");
        assertThat(parsed.getPayload().get("userId")).isEqualTo(123);
        assertThat(parsed.getPayload().get("email")).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("parseToken() should throw exception for invalid token")
    void parseToken_WithInvalidToken_ThrowsException() {
        String invalidToken = "invalid.token.here";

        org.junit.jupiter.api.Assertions.assertThrows(
                io.jsonwebtoken.JwtException.class,
                () -> jwtTemplate.parseToken(invalidToken)
        );
    }

    @Test
    @DisplayName("parseToken() should throw exception for tampered token")
    void parseToken_WithTamperedToken_ThrowsException() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "testuser");

        String token = jwtTemplate.generateToken(claims);
        String tamperedToken = token.substring(0, token.length() - 5) + "xxxxx";

        org.junit.jupiter.api.Assertions.assertThrows(
                io.jsonwebtoken.JwtException.class,
                () -> jwtTemplate.parseToken(tamperedToken)
        );
    }

    @Test
    @DisplayName("generateToken() should set issuedAt and expiration dates")
    void generateToken_SetsIssuedAtAndExpiration() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "testuser");

        String token = jwtTemplate.generateToken(claims);
        Jws<Claims> parsed = jwtTemplate.parseToken(token);

        assertThat(parsed.getPayload().getIssuedAt()).isNotNull();
        assertThat(parsed.getPayload().getExpiration()).isNotNull();
    }

    @Test
    @DisplayName("Token should contain all provided claims")
    void generateToken_ContainsAllProvidedClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("stringClaim", "value");
        claims.put("intClaim", 42);
        claims.put("boolClaim", true);
        claims.put("listClaim", java.util.List.of("a", "b", "c"));

        String token = jwtTemplate.generateToken(claims);
        Jws<Claims> parsed = jwtTemplate.parseToken(token);

        assertThat(parsed.getPayload().get("stringClaim")).isEqualTo("value");
        assertThat(parsed.getPayload().get("intClaim")).isEqualTo(42);
        assertThat(parsed.getPayload().get("boolClaim")).isEqualTo(true);
    }
}
