package com.springddd.application.service.auth.jwt;

import com.springddd.application.service.auth.SecurityProperties;
import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.user.UserId;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JwtAuthenticationConverterTest {

    @Mock
    private JwtTemplate jwtTemplate;

    @Mock
    private SecurityProperties securityProperties;

    @Mock
    private ReactiveRedisCacheHelper reactiveRedisCacheHelper;

    @Mock
    private ServerWebExchange exchange;

    @Mock
    private ServerHttpRequest request;

    private JwtAuthenticationConverter converter;

    @BeforeEach
    void setUp() {
        converter = new JwtAuthenticationConverter(jwtTemplate, securityProperties, reactiveRedisCacheHelper);
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());
        when(exchange.getRequest()).thenReturn(request);
        when(request.getHeaders()).thenReturn(new HttpHeaders());
    }

    @Test
    void convert_shouldError_whenAuthHeaderMissing() {
        when(request.getHeaders()).thenReturn(new HttpHeaders());

        StepVerifier.create(converter.convert(exchange))
                .expectError(AccessDeniedException.class)
                .verify();
    }

    @Test
    void convert_shouldError_whenAuthHeaderInvalid() {
        when(request.getHeaders()).thenReturn(new HttpHeaders() {{ add(HttpHeaders.AUTHORIZATION, "Basic abc"); }});

        StepVerifier.create(converter.convert(exchange))
                .expectError(AccessDeniedException.class)
                .verify();
    }

    @Test
    void convert_shouldError_whenTokenExpiredInCache() {
        String token = "validToken";
        when(request.getHeaders()).thenReturn(new HttpHeaders() {{ add(HttpHeaders.AUTHORIZATION, "Bearer " + token); }});

        @SuppressWarnings("unchecked")
        Jws<Claims> jws = mock(Jws.class);
        Claims claims = mock(Claims.class);
        when(claims.get("userId", Long.class)).thenReturn(1L);
        when(jws.getPayload()).thenReturn(claims);
        when(jwtTemplate.parseToken(token)).thenReturn(jws);

        when(reactiveRedisCacheHelper.getCache(CacheKeys.USER_TOKEN.buildKey(1L), String.class))
                .thenReturn(Mono.empty());

        StepVerifier.create(converter.convert(exchange))
                .expectError(AccessDeniedException.class)
                .verify();
    }

    @Test
    void convert_shouldError_whenTokenMismatch() {
        String token = "validToken";
        when(request.getHeaders()).thenReturn(new HttpHeaders() {{ add(HttpHeaders.AUTHORIZATION, "Bearer " + token); }});

        @SuppressWarnings("unchecked")
        Jws<Claims> jws = mock(Jws.class);
        Claims claims = mock(Claims.class);
        when(claims.get("userId", Long.class)).thenReturn(1L);
        when(jws.getPayload()).thenReturn(claims);
        when(jwtTemplate.parseToken(token)).thenReturn(jws);

        when(reactiveRedisCacheHelper.getCache(CacheKeys.USER_TOKEN.buildKey(1L), String.class))
                .thenReturn(Mono.just("otherToken"));

        StepVerifier.create(converter.convert(exchange))
                .expectError(AccessDeniedException.class)
                .verify();
    }

    @Test
    void convert_shouldReturnAuthentication_whenTokenValid() {
        String token = "validToken";
        when(request.getHeaders()).thenReturn(new HttpHeaders() {{ add(HttpHeaders.AUTHORIZATION, "Bearer " + token); }});

        @SuppressWarnings("unchecked")
        Jws<Claims> jws = mock(Jws.class);
        Claims claims = mock(Claims.class);
        when(claims.get("userId", Long.class)).thenReturn(1L);
        when(jws.getPayload()).thenReturn(claims);
        when(jwtTemplate.parseToken(token)).thenReturn(jws);

        when(reactiveRedisCacheHelper.getCache(CacheKeys.USER_TOKEN.buildKey(1L), String.class))
                .thenReturn(Mono.just(token));

        AuthUser user = new AuthUser();
        user.setUserId(new UserId(1L));
        user.setUsername("admin");
        user.setPermissions(List.of("sys:user:list"));

        when(reactiveRedisCacheHelper.getCache(CacheKeys.USER_DETAIL.buildKey(1L), AuthUser.class))
                .thenReturn(Mono.just(user));

        StepVerifier.create(converter.convert(exchange))
                .assertNext(auth -> {
                    assertTrue(auth instanceof UsernamePasswordAuthenticationToken);
                    assertTrue(auth.isAuthenticated());
                })
                .verifyComplete();
    }

    @Test
    void convert_shouldReturnEmpty_forIgnoredPath() {
        when(securityProperties.getIgnorePaths()).thenReturn(List.of("/api/auth/login"));
        when(request.getPath()).thenReturn(org.springframework.http.server.RequestPath.parse("/api/auth/login", ""));

        StepVerifier.create(converter.convert(exchange))
                .verifyComplete();
    }

    @Test
    void convert_shouldSkipUserDetailCheck_forTokenOnlyPath() {
        String token = "validToken";
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of("/api/token"));
        when(request.getPath()).thenReturn(org.springframework.http.server.RequestPath.parse("/api/token", ""));
        when(request.getHeaders()).thenReturn(new HttpHeaders() {{ add(HttpHeaders.AUTHORIZATION, "Bearer " + token); }});

        @SuppressWarnings("unchecked")
        Jws<Claims> jws = mock(Jws.class);
        Claims claims = mock(Claims.class);
        when(claims.get("userId", Long.class)).thenReturn(1L);
        when(jws.getPayload()).thenReturn(claims);
        when(jwtTemplate.parseToken(token)).thenReturn(jws);

        when(reactiveRedisCacheHelper.getCache(CacheKeys.USER_TOKEN.buildKey(1L), String.class))
                .thenReturn(Mono.just(token));

        AuthUser user = new AuthUser();
        user.setUserId(new UserId(1L));
        user.setUsername("admin");
        user.setPermissions(List.of("sys:user:list"));

        when(reactiveRedisCacheHelper.getCache(CacheKeys.USER_DETAIL.buildKey(1L), AuthUser.class))
                .thenReturn(Mono.just(user));

        StepVerifier.create(converter.convert(exchange))
                .assertNext(auth -> {
                    assertTrue(auth instanceof UsernamePasswordAuthenticationToken);
                })
                .verifyComplete();
    }

    @Test
    void convert_shouldError_whenGetCacheThrowsException() {
        String token = "validToken";
        when(request.getHeaders()).thenReturn(new HttpHeaders() {{ add(HttpHeaders.AUTHORIZATION, "Bearer " + token); }});

        @SuppressWarnings("unchecked")
        Jws<Claims> jws = mock(Jws.class);
        Claims claims = mock(Claims.class);
        when(claims.get("userId", Long.class)).thenReturn(1L);
        when(jws.getPayload()).thenReturn(claims);
        when(jwtTemplate.parseToken(token)).thenReturn(jws);

        when(reactiveRedisCacheHelper.getCache(CacheKeys.USER_TOKEN.buildKey(1L), String.class))
                .thenReturn(Mono.just(token));

        when(reactiveRedisCacheHelper.getCache(CacheKeys.USER_DETAIL.buildKey(1L), AuthUser.class))
                .thenThrow(new RuntimeException("cache error"));

        StepVerifier.create(converter.convert(exchange))
                .expectError(AccessDeniedException.class)
                .verify();
    }
}
