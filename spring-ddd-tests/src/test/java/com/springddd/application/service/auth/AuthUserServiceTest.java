package com.springddd.application.service.auth;

import com.springddd.application.service.auth.dto.LoginUserQuery;
import com.springddd.application.service.auth.dto.LoginUserView;
import com.springddd.application.service.auth.dto.UserInfoView;
import com.springddd.application.service.auth.handler.AuthHandler;
import com.springddd.application.service.auth.jwt.JwtSecret;
import com.springddd.application.service.auth.jwt.JwtTemplate;
import com.springddd.domain.auth.AuthUser;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.CacheProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class AuthUserServiceTest {

    @Mock
    private AuthHandler authHandler;

    @Mock
    private JwtTemplate jwtTemplate;

    @Mock
    private CacheProcessor cacheProcessor;

    @Mock
    private JwtSecret jwtSecret;

    @InjectMocks
    private AuthUserService service;

    @BeforeEach
    void setUp() {
        when(jwtSecret.getTtl()).thenReturn(7);
    }

    @Test
    @DisplayName("getToken 应返回带 accessToken 的视图")
    void getToken_shouldReturnTokenView() {
        LoginUserQuery query = new LoginUserQuery();
        query.setUsername("admin");
        query.setPassword("123456");

        AuthUser user = new AuthUser();
        user.setUserId(new com.springddd.domain.user.UserId(1L));
        user.setUsername("admin");
        user.setPermissions(List.of("sys:user:list"));

        when(authHandler.handle(query)).thenReturn(Mono.just(user));
        when(jwtTemplate.generateToken(anyMap())).thenReturn("mock-token");
        when(cacheProcessor.deleteCache(anyString())).thenReturn(Mono.empty());
        when(cacheProcessor.setCache(anyString(), any(), any(Duration.class))).thenReturn(Mono.empty());

        StepVerifier.create(service.getToken(query))
                .assertNext(view -> assertThat(view.getAccessToken()).isEqualTo("mock-token"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getUserInfo 应返回用户信息视图")
    void getUserInfo_shouldReturnUserInfo() {
        StepVerifier.create(service.getUserInfo())
                .assertNext(view -> assertThat(view).isNotNull())
                .verifyComplete();
    }

    @Test
    @DisplayName("getUserPermissions 应返回权限列表")
    void getUserPermissions_shouldReturnPermissions() {
        StepVerifier.create(service.getUserPermissions())
                .assertNext(list -> assertThat(list).isNotNull())
                .verifyComplete();
    }

    @Test
    @DisplayName("clearCache 应删除用户缓存")
    void clearCache_shouldDeleteCache() {
        when(cacheProcessor.deleteCache(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(service.clearCache())
                .verifyComplete();

        verify(cacheProcessor).deleteCache(anyString());
    }
}
