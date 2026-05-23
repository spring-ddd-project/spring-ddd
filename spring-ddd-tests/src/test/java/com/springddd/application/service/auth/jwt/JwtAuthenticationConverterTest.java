package com.springddd.application.service.auth.jwt;

import com.springddd.application.service.auth.SecurityProperties;
import com.springddd.domain.auth.AuthUser;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.CacheProcessor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationConverterTest {

    @Mock
    private JwtTemplate jwtTemplate;

    @Mock
    private SecurityProperties securityProperties;

    @Mock
    private CacheProcessor cacheProcessor;

    private JwtAuthenticationConverter converter;

    private String validToken;
    private AuthUser authUser;

    @BeforeEach
    void setUp() {
        converter = new JwtAuthenticationConverter(jwtTemplate, securityProperties, cacheProcessor);
        validToken = Jwts.builder()
                .claim("userId", 1L)
                .signWith(Keys.hmacShaKeyFor("my-secret-key-my-secret-key-my-secret-key".getBytes()), Jwts.SIG.HS256)
                .compact();

        authUser = new AuthUser();
        authUser.setUserId(new com.springddd.domain.user.UserId(1L));
        authUser.setUsername("test");
        authUser.setRoles(List.of("admin"));
    }

    @Test
    @DisplayName("convert 对忽略路径应返回 empty")
    void convert_whenIgnoredPath_shouldReturnEmpty() {
        when(securityProperties.getIgnorePaths()).thenReturn(List.of("/auth/login"));

        MockServerHttpRequest request = MockServerHttpRequest.get("/auth/login").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(converter.convert(exchange))
                .verifyComplete();
    }

    @Test
    @DisplayName("convert 当缺少 Authorization header 时应返回错误")
    void convert_whenMissingAuthHeader_shouldReturnError() {
        when(securityProperties.getIgnorePaths()).thenReturn(Collections.emptyList());

        MockServerHttpRequest request = MockServerHttpRequest.get("/api/test").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(converter.convert(exchange))
                .expectError(AccessDeniedException.class)
                .verify();
    }

    @Test
    @DisplayName("convert 当 Authorization header 格式无效时应返回错误")
    void convert_whenInvalidAuthHeaderFormat_shouldReturnError() {
        when(securityProperties.getIgnorePaths()).thenReturn(Collections.emptyList());

        MockServerHttpRequest request = MockServerHttpRequest.get("/api/test")
                .header(HttpHeaders.AUTHORIZATION, "Basic dXNlcjpwYXNz")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(converter.convert(exchange))
                .expectError(AccessDeniedException.class)
                .verify();
    }

    @Test
    @DisplayName("convert 当缓存 token 不匹配时应返回错误")
    void convert_whenCachedTokenMismatch_shouldReturnError() {
        when(securityProperties.getIgnorePaths()).thenReturn(Collections.emptyList());

        MockServerHttpRequest request = MockServerHttpRequest.get("/api/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        Jws<Claims> claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor("my-secret-key-my-secret-key-my-secret-key".getBytes()))
                .build()
                .parseSignedClaims(validToken);
        when(jwtTemplate.parseToken(validToken)).thenReturn(claims);
        when(cacheProcessor.getCache(CacheKeys.USER_TOKEN.buildKey(1L), String.class))
                .thenReturn(reactor.core.publisher.Mono.just("different-token"));

        StepVerifier.create(converter.convert(exchange))
                .expectError(AccessDeniedException.class)
                .verify();
    }

    @Test
    @DisplayName("convert 当缓存 token 不存在时应返回错误")
    void convert_whenCachedTokenNotFound_shouldReturnError() {
        when(securityProperties.getIgnorePaths()).thenReturn(Collections.emptyList());

        MockServerHttpRequest request = MockServerHttpRequest.get("/api/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        Jws<Claims> claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor("my-secret-key-my-secret-key-my-secret-key".getBytes()))
                .build()
                .parseSignedClaims(validToken);
        when(jwtTemplate.parseToken(validToken)).thenReturn(claims);
        when(cacheProcessor.getCache(CacheKeys.USER_TOKEN.buildKey(1L), String.class))
                .thenReturn(reactor.core.publisher.Mono.empty());

        StepVerifier.create(converter.convert(exchange))
                .expectError(AccessDeniedException.class)
                .verify();
    }

    @Test
    @DisplayName("convert 当用户详情缓存不存在时应返回错误")
    void convert_whenUserDetailCacheNotFound_shouldReturnError() {
        when(securityProperties.getIgnorePaths()).thenReturn(Collections.emptyList());

        MockServerHttpRequest request = MockServerHttpRequest.get("/api/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        Jws<Claims> claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor("my-secret-key-my-secret-key-my-secret-key".getBytes()))
                .build()
                .parseSignedClaims(validToken);
        when(jwtTemplate.parseToken(validToken)).thenReturn(claims);
        when(cacheProcessor.getCache(CacheKeys.USER_TOKEN.buildKey(1L), String.class))
                .thenReturn(reactor.core.publisher.Mono.just(validToken));
        when(cacheProcessor.getCache(CacheKeys.USER_DETAIL.buildKey(1L), AuthUser.class))
                .thenReturn(reactor.core.publisher.Mono.empty());

        StepVerifier.create(converter.convert(exchange))
                .expectError(AccessDeniedException.class)
                .verify();
    }

    @Test
    @DisplayName("convert 当 token 匹配时应返回 Authentication")
    void convert_whenTokenMatches_shouldReturnAuthentication() {
        when(securityProperties.getIgnorePaths()).thenReturn(Collections.emptyList());

        MockServerHttpRequest request = MockServerHttpRequest.get("/api/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        Jws<Claims> claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor("my-secret-key-my-secret-key-my-secret-key".getBytes()))
                .build()
                .parseSignedClaims(validToken);
        when(jwtTemplate.parseToken(validToken)).thenReturn(claims);
        when(cacheProcessor.getCache(CacheKeys.USER_TOKEN.buildKey(1L), String.class))
                .thenReturn(reactor.core.publisher.Mono.just(validToken));
        when(cacheProcessor.getCache(CacheKeys.USER_DETAIL.buildKey(1L), AuthUser.class))
                .thenReturn(reactor.core.publisher.Mono.just(authUser));

        StepVerifier.create(converter.convert(exchange))
                .assertNext(auth -> {
                    assertThat(auth).isNotNull();
                    assertThat(auth.getPrincipal()).isEqualTo(authUser);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("convert 当用户详情缓存抛出异常时应返回错误")
    void convert_whenUserDetailCacheThrowsException_shouldReturnError() {
        when(securityProperties.getIgnorePaths()).thenReturn(Collections.emptyList());

        MockServerHttpRequest request = MockServerHttpRequest.get("/api/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        Jws<Claims> claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor("my-secret-key-my-secret-key-my-secret-key".getBytes()))
                .build()
                .parseSignedClaims(validToken);
        when(jwtTemplate.parseToken(validToken)).thenReturn(claims);
        when(cacheProcessor.getCache(CacheKeys.USER_TOKEN.buildKey(1L), String.class))
                .thenReturn(reactor.core.publisher.Mono.just(validToken));
        when(cacheProcessor.getCache(CacheKeys.USER_DETAIL.buildKey(1L), AuthUser.class))
                .thenThrow(new RuntimeException("cache connection failed"));

        StepVerifier.create(converter.convert(exchange))
                .expectError(AccessDeniedException.class)
                .verify();
    }
}
