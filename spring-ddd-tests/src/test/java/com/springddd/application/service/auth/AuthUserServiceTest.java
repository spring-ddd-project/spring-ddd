package com.springddd.application.service.auth;

import com.springddd.application.service.auth.dto.LoginUserQuery;
import com.springddd.application.service.auth.dto.LoginUserView;
import com.springddd.application.service.auth.dto.UserInfoView;
import com.springddd.application.service.auth.jwt.JwtSecret;
import com.springddd.application.service.auth.jwt.JwtTemplate;
import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.user.UserId;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
        authUserService = new AuthUserService(authenticationManager, jwtTemplate, reactiveRedisCacheHelper, jwtSecret);
    }

    @Test
    void getToken_shouldReturnLoginUserView_whenUserHasPermissions() {
        LoginUserQuery query = new LoginUserQuery();
        query.setUsername("admin");
        query.setPassword("password");

        AuthUser user = new AuthUser();
        user.setUserId(new UserId(1L));
        user.setUsername("admin");
        user.setPermissions(List.of("sys:user:list"));

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mono.just(auth));
        when(jwtTemplate.generateToken(any())).thenReturn("mockToken");
        when(jwtSecret.getTtl()).thenReturn(7);
        when(reactiveRedisCacheHelper.deleteCache(any())).thenReturn(Mono.empty());
        when(reactiveRedisCacheHelper.setCache(any(), any(), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(authUserService.getToken(query))
                .assertNext(view -> {
                    assertNotNull(view);
                    assertEquals("mockToken", view.getAccessToken());
                })
                .verifyComplete();
    }

    @Test
    void getToken_shouldReturnLoginUserView_whenUserHasNoPermissions() {
        LoginUserQuery query = new LoginUserQuery();
        query.setUsername("admin");
        query.setPassword("password");

        AuthUser user = new AuthUser();
        user.setUserId(new UserId(1L));
        user.setUsername("admin");
        user.setPermissions(List.of());

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mono.just(auth));
        when(jwtTemplate.generateToken(any())).thenReturn("mockToken");

        StepVerifier.create(authUserService.getToken(query))
                .assertNext(view -> assertEquals("mockToken", view.getAccessToken()))
                .verifyComplete();
    }

    @Test
    void getUserInfo_shouldReturnUserInfoView() {
        SecurityUtils.setUserId(1L);
        SecurityUtils.setUsername("admin");
        SecurityUtils.setRoles(List.of("ROLE_ADMIN"));

        StepVerifier.create(authUserService.getUserInfo())
                .assertNext(view -> {
                    assertEquals("admin", view.getRealName());
                    assertEquals(List.of("ROLE_ADMIN"), view.getRoles());
                })
                .verifyComplete();
    }

    @Test
    void getUserPermissions_shouldReturnPermissions() {
        SecurityUtils.setPermissions(List.of("sys:user:list", "sys:user:create"));

        StepVerifier.create(authUserService.getUserPermissions())
                .assertNext(perms -> {
                    assertEquals(2, perms.size());
                    assertTrue(perms.contains("sys:user:list"));
                })
                .verifyComplete();
    }

    @Test
    void clearCache_shouldDeleteUserCache() {
        SecurityUtils.setUserId(1L);
        when(reactiveRedisCacheHelper.deleteCache(any())).thenReturn(Mono.empty());

        StepVerifier.create(authUserService.clearCache())
                .verifyComplete();
    }
}
