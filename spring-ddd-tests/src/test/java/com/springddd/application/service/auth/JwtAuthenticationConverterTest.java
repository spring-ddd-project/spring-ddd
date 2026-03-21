package com.springddd.application.service.auth;

import com.springddd.application.service.auth.jwt.JwtAuthenticationConverter;
import com.springddd.application.service.auth.jwt.JwtSecret;
import com.springddd.application.service.auth.jwt.JwtTemplate;
import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.user.UserId;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("JwtAuthenticationConverter Tests")
class JwtAuthenticationConverterTest {

    private JwtAuthenticationConverter jwtAuthenticationConverter;

    @Mock
    private JwtTemplate jwtTemplate;

    @Mock
    private SecurityProperties securityProperties;

    @Mock
    private ReactiveRedisCacheHelper reactiveRedisCacheHelper;

    private static final String SECRET_KEY = "this-is-a-very-long-secret-key-for-testing-purposes-at-least-256-bits";
    private static final Integer TTL = 30;

    private AuthUser testUser;
    private String validToken;
    private Jws<Claims> mockClaims;

    @BeforeEach
    void setUp() {
        JwtSecret jwtSecret = mock(JwtSecret.class);
        when(jwtSecret.getKey()).thenReturn(SECRET_KEY);
        when(jwtSecret.getTtl()).thenReturn(TTL);

        jwtAuthenticationConverter = new JwtAuthenticationConverter(
                jwtTemplate,
                securityProperties,
                reactiveRedisCacheHelper
        );

        testUser = new AuthUser();
        testUser.setUserId(new UserId(1L));
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRoles(List.of("ROLE_USER"));
        testUser.setPermissions(List.of("user:read"));

        validToken = "valid.jwt.token";

        mockClaims = mock(Jws.class);
        Claims claims = mock(Claims.class);
        when(mockClaims.getPayload()).thenReturn(claims);
        when(claims.get("userId", Long.class)).thenReturn(1L);
    }

    @AfterEach
    void tearDown() {
        SecurityUtils.setUserId(null);
        SecurityUtils.setUsername(null);
        SecurityUtils.setRoles(null);
        SecurityUtils.setPermissions(null);
        SecurityUtils.setMenuIds(null);
    }

    @Test
    @DisplayName("convert() should return error for missing Authorization header")
    void convert_WithMissingAuthHeader_ReturnsError() {
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        org.springframework.http.server.reactive.ServerHttpRequest request = mock(org.springframework.http.server.reactive.ServerHttpRequest.class);

        when(exchange.getRequest()).thenReturn(request);
        when(request.getHeaders()).thenReturn(new HttpHeaders());
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());

        StepVerifier.create(jwtAuthenticationConverter.convert(exchange))
                .expectError(AccessDeniedException.class)
                .verify();
    }

    @Test
    @DisplayName("convert() should return error for invalid Authorization header format")
    void convert_WithInvalidAuthHeaderFormat_ReturnsError() {
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        org.springframework.http.server.reactive.ServerHttpRequest request = mock(org.springframework.http.server.reactive.ServerHttpRequest.class);
        HttpHeaders headers = new HttpHeaders();

        when(exchange.getRequest()).thenReturn(request);
        when(request.getHeaders()).thenReturn(headers);
        headers.set(HttpHeaders.AUTHORIZATION, "InvalidFormat");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());

        StepVerifier.create(jwtAuthenticationConverter.convert(exchange))
                .expectError(AccessDeniedException.class)
                .verify();
    }

    @Test
    @DisplayName("convert() should return error when token is expired from cache")
    void convert_WithExpiredToken_ReturnsError() {
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        org.springframework.http.server.reactive.ServerHttpRequest request = mock(org.springframework.http.server.reactive.ServerHttpRequest.class);
        HttpHeaders headers = new HttpHeaders();

        when(exchange.getRequest()).thenReturn(request);
        when(request.getHeaders()).thenReturn(headers);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + validToken);
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());
        when(jwtTemplate.parseToken(validToken)).thenReturn(mockClaims);
        when(reactiveRedisCacheHelper.getCache(eq(CacheKeys.USER_TOKEN.buildKey(1L)), eq(String.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(jwtAuthenticationConverter.convert(exchange))
                .expectError(AccessDeniedException.class)
                .verify();
    }

    @Test
    @DisplayName("convert() should return error when token does not match cached token")
    void convert_WithMismatchedToken_ReturnsError() {
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        org.springframework.http.server.reactive.ServerHttpRequest request = mock(org.springframework.http.server.reactive.ServerHttpRequest.class);
        HttpHeaders headers = new HttpHeaders();

        when(exchange.getRequest()).thenReturn(request);
        when(request.getHeaders()).thenReturn(headers);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + validToken);
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());
        when(jwtTemplate.parseToken(validToken)).thenReturn(mockClaims);
        when(reactiveRedisCacheHelper.getCache(eq(CacheKeys.USER_TOKEN.buildKey(1L)), eq(String.class)))
                .thenReturn(Mono.just("different-token"));

        StepVerifier.create(jwtAuthenticationConverter.convert(exchange))
                .expectError(AccessDeniedException.class)
                .verify();
    }

    @Test
    @DisplayName("convert() should return authentication for valid token")
    void convert_WithValidToken_ReturnsAuthentication() {
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        org.springframework.http.server.reactive.ServerHttpRequest request = mock(org.springframework.http.server.reactive.ServerHttpRequest.class);
        HttpHeaders headers = new HttpHeaders();

        when(exchange.getRequest()).thenReturn(request);
        when(request.getHeaders()).thenReturn(headers);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + validToken);
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());
        when(jwtTemplate.parseToken(validToken)).thenReturn(mockClaims);
        when(reactiveRedisCacheHelper.getCache(eq(CacheKeys.USER_TOKEN.buildKey(1L)), eq(String.class)))
                .thenReturn(Mono.just(validToken));
        when(reactiveRedisCacheHelper.getCache(eq(CacheKeys.USER_DETAIL.buildKey(1L)), eq(AuthUser.class)))
                .thenReturn(Mono.just(testUser));

        StepVerifier.create(jwtAuthenticationConverter.convert(exchange))
                .assertNext(auth -> {
                    assertThat(auth).isInstanceOf(UsernamePasswordAuthenticationToken.class);
                    assertThat(auth.isAuthenticated()).isTrue();
                    assertThat(auth.getPrincipal()).isEqualTo(testUser);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("convert() should return error when user detail not found in cache")
    void convert_WithUserDetailNotFound_ReturnsError() {
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        org.springframework.http.server.reactive.ServerHttpRequest request = mock(org.springframework.http.server.reactive.ServerHttpRequest.class);
        HttpHeaders headers = new HttpHeaders();

        when(exchange.getRequest()).thenReturn(request);
        when(request.getHeaders()).thenReturn(headers);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + validToken);
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());
        when(jwtTemplate.parseToken(validToken)).thenReturn(mockClaims);
        when(reactiveRedisCacheHelper.getCache(eq(CacheKeys.USER_TOKEN.buildKey(1L)), eq(String.class)))
                .thenReturn(Mono.just(validToken));
        when(reactiveRedisCacheHelper.getCache(eq(CacheKeys.USER_DETAIL.buildKey(1L)), eq(AuthUser.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(jwtAuthenticationConverter.convert(exchange))
                .expectError(AccessDeniedException.class)
                .verify();
    }

    @Test
    @DisplayName("convert() should handle token with Bearer prefix and spaces")
    void convert_WithBearerPrefixAndSpaces_ReturnsAuthentication() {
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        org.springframework.http.server.reactive.ServerHttpRequest request = mock(org.springframework.http.server.reactive.ServerHttpRequest.class);
        HttpHeaders headers = new HttpHeaders();

        when(exchange.getRequest()).thenReturn(request);
        when(request.getHeaders()).thenReturn(headers);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer   " + validToken + "   ");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());
        when(jwtTemplate.parseToken(validToken)).thenReturn(mockClaims);
        when(reactiveRedisCacheHelper.getCache(eq(CacheKeys.USER_TOKEN.buildKey(1L)), eq(String.class)))
                .thenReturn(Mono.just(validToken));
        when(reactiveRedisCacheHelper.getCache(eq(CacheKeys.USER_DETAIL.buildKey(1L)), eq(AuthUser.class)))
                .thenReturn(Mono.just(testUser));

        StepVerifier.create(jwtAuthenticationConverter.convert(exchange))
                .assertNext(auth -> {
                    assertThat(auth).isInstanceOf(UsernamePasswordAuthenticationToken.class);
                    assertThat(auth.isAuthenticated()).isTrue();
                })
                .verifyComplete();
    }
}
