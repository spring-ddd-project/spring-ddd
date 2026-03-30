package com.springddd.application.service.auth;

import com.springddd.application.service.auth.dto.LoginUserQuery;
import com.springddd.application.service.auth.dto.LoginUserView;
import com.springddd.application.service.auth.dto.UserInfoView;
import com.springddd.application.service.auth.jwt.JwtSecret;
import com.springddd.application.service.auth.jwt.JwtTemplate;
import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthUserServiceTest {

    @Mock
    private ReactiveAuthenticationManager authenticationManager;

    @Mock
    private JwtTemplate jwtTemplate;

    @Mock
    private ReactiveRedisCacheHelper reactiveRedisCacheHelper;

    @Mock
    private JwtSecret jwtSecret;

    private AuthUserService authUserService;

    @BeforeEach
    void setUp() {
        authUserService = new AuthUserService(
                authenticationManager,
                jwtTemplate,
                reactiveRedisCacheHelper,
                jwtSecret
        );
    }

    @Test
    void getToken_shouldReturnToken_whenAuthenticationSucceeds() {
        LoginUserQuery query = new LoginUserQuery();
        query.setUsername("testuser");
        query.setPassword("password");

        AuthUser authUser = new AuthUser();
        authUser.setUserId(new com.springddd.domain.user.UserId(1L));
        authUser.setUsername("testuser");
        authUser.setPermissions(Collections.emptyList());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authUser, null, Collections.emptyList());

        when(authenticationManager.authenticate(any())).thenReturn(Mono.just(authentication));
        when(jwtTemplate.generateToken(anyMap())).thenReturn("test-token");
        when(jwtSecret.getTtl()).thenReturn(7);
        when(reactiveRedisCacheHelper.deleteCache(any())).thenReturn(Mono.empty());
        when(reactiveRedisCacheHelper.setCache(any(), any(), any(Duration.class))).thenReturn(Mono.empty());

        Mono<LoginUserView> result = authUserService.getToken(query);

        StepVerifier.create(result)
                .expectNextMatches(view -> "test-token".equals(view.getAccessToken()))
                .verifyComplete();
    }

    @Test
    void getToken_shouldCacheUserDetails_whenUserHasPermissions() {
        LoginUserQuery query = new LoginUserQuery();
        query.setUsername("testuser");
        query.setPassword("password");

        AuthUser authUser = new AuthUser();
        authUser.setUserId(new com.springddd.domain.user.UserId(1L));
        authUser.setUsername("testuser");
        authUser.setPermissions(Arrays.asList("perm1", "perm2"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authUser, null, Collections.emptyList());

        when(authenticationManager.authenticate(any())).thenReturn(Mono.just(authentication));
        when(jwtTemplate.generateToken(anyMap())).thenReturn("test-token");
        when(jwtSecret.getTtl()).thenReturn(7);
        when(reactiveRedisCacheHelper.deleteCache(any())).thenReturn(Mono.empty());
        when(reactiveRedisCacheHelper.setCache(any(), any(), any(Duration.class))).thenReturn(Mono.empty());

        Mono<LoginUserView> result = authUserService.getToken(query);

        StepVerifier.create(result)
                .expectNextMatches(view -> "test-token".equals(view.getAccessToken()))
                .verifyComplete();

        verify(reactiveRedisCacheHelper, times(2)).setCache(any(), any(), any(Duration.class));
    }

    @Test
    void getUserPermissions_shouldReturnPermissions() {
        reactor.core.publisher.Mono<List<String>> result = authUserService.getUserPermissions();

        StepVerifier.create(result)
                .expectNextMatches(permissions -> permissions != null)
                .verifyComplete();
    }

    @Test
    void clearCache_shouldDeleteCache() {
        when(reactiveRedisCacheHelper.deleteCache(any())).thenReturn(Mono.empty());

        Mono<Void> result = authUserService.clearCache();

        StepVerifier.create(result)
                .verifyComplete();

        verify(reactiveRedisCacheHelper).deleteCache(any());
    }
}
