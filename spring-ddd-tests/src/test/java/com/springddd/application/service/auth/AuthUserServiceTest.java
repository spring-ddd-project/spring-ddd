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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthUserService Tests")
class AuthUserServiceTest {

    @InjectMocks
    private AuthUserService authUserService;

    @Mock
    private ReactiveAuthenticationManager authenticationManager;

    @Mock
    private JwtTemplate jwtTemplate;

    @Mock
    private ReactiveRedisCacheHelper reactiveRedisCacheHelper;

    @Mock
    private JwtSecret jwtSecret;

    private AuthUser testUser;
    private LoginUserQuery testQuery;

    @BeforeEach
    void setUp() {
        testUser = new AuthUser();
        testUser.setUserId(new UserId(1L));
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRoles(List.of("ROLE_USER"));
        testUser.setPermissions(List.of("user:read", "user:write"));
        testUser.setMenuIds(List.of(1L, 2L));

        testQuery = new LoginUserQuery();
        testQuery.setUsername("testuser");
        testQuery.setPassword("password");
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
    @DisplayName("getToken() should return login view with access token on successful authentication")
    void getToken_WithValidCredentials_ReturnsLoginUserView() {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                testUser, "credentials", testUser.getAuthorities()
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mono.just(authToken));
        when(jwtTemplate.generateToken(any())).thenReturn("test-jwt-token");
        when(jwtSecret.getTtl()).thenReturn(30);
        when(reactiveRedisCacheHelper.deleteCache(anyString())).thenReturn(Mono.empty());
        when(reactiveRedisCacheHelper.setCache(anyString(), any(), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(authUserService.getToken(testQuery))
                .assertNext(view -> {
                    assertThat(view).isNotNull();
                    assertThat(view.getAccessToken()).isEqualTo("test-jwt-token");
                })
                .verifyComplete();

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTemplate).generateToken(any());
    }

    @Test
    @DisplayName("getToken() should cache user info when permissions are not empty")
    void getToken_WithPermissions_CachesUserInfo() {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                testUser, "credentials", testUser.getAuthorities()
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mono.just(authToken));
        when(jwtTemplate.generateToken(any())).thenReturn("test-jwt-token");
        when(jwtSecret.getTtl()).thenReturn(30);
        when(reactiveRedisCacheHelper.deleteCache(anyString())).thenReturn(Mono.empty());
        when(reactiveRedisCacheHelper.setCache(anyString(), any(), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(authUserService.getToken(testQuery))
                .assertNext(view -> {
                    assertThat(view.getAccessToken()).isEqualTo("test-jwt-token");
                })
                .verifyComplete();

        verify(reactiveRedisCacheHelper).deleteCache(eq(CacheKeys.USER_TOKEN.buildKey(1L)));
        verify(reactiveRedisCacheHelper, times(2)).setCache(anyString(), any(), any(Duration.class));
    }

    @Test
    @DisplayName("getToken() should not cache when user has no permissions")
    void getToken_WithNoPermissions_DoesNotCache() {
        testUser.setPermissions(null);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                testUser, "credentials", testUser.getAuthorities()
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mono.just(authToken));
        when(jwtTemplate.generateToken(any())).thenReturn("test-jwt-token");

        StepVerifier.create(authUserService.getToken(testQuery))
                .assertNext(view -> {
                    assertThat(view.getAccessToken()).isEqualTo("test-jwt-token");
                })
                .verifyComplete();

        verify(reactiveRedisCacheHelper, never()).deleteCache(anyString());
        verify(reactiveRedisCacheHelper, never()).setCache(anyString(), any(), any(Duration.class));
    }

    @Test
    @DisplayName("getUserInfo() should return user info from SecurityUtils")
    void getUserInfo_ReturnsUserInfoFromSecurityContext() {
        SecurityUtils.setAuthUserContext(testUser);

        StepVerifier.create(authUserService.getUserInfo())
                .assertNext(view -> {
                    assertThat(view).isNotNull();
                    assertThat(view.getRealName()).isEqualTo("testuser");
                    assertThat(view.getRoles()).containsExactly("ROLE_USER");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("getUserInfo() should return empty roles when no roles set")
    void getUserInfo_WithNoRoles_ReturnsEmptyRoles() {
        SecurityUtils.setUsername("testuser");
        SecurityUtils.setRoles(List.of());

        StepVerifier.create(authUserService.getUserInfo())
                .assertNext(view -> {
                    assertThat(view.getRealName()).isEqualTo("testuser");
                    assertThat(view.getRoles()).isEmpty();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("getUserPermissions() should return permissions from SecurityUtils")
    void getUserPermissions_ReturnsPermissionsFromSecurityContext() {
        SecurityUtils.setPermissions(List.of("user:read", "user:write", "user:delete"));

        StepVerifier.create(authUserService.getUserPermissions())
                .assertNext(permissions -> {
                    assertThat(permissions).containsExactly("user:read", "user:write", "user:delete");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("getUserPermissions() should return empty list when no permissions")
    void getUserPermissions_WithNoPermissions_ReturnsEmptyList() {
        SecurityUtils.setPermissions(List.of());

        StepVerifier.create(authUserService.getUserPermissions())
                .assertNext(permissions -> {
                    assertThat(permissions).isEmpty();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("clearCache() should delete all user caches")
    void clearCache_DeletesUserCaches() {
        SecurityUtils.setUserId(1L);
        when(reactiveRedisCacheHelper.deleteCache(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(authUserService.clearCache())
                .verifyComplete();

        verify(reactiveRedisCacheHelper).deleteCache(eq(CacheKeys.USER_ALL.buildKey(1L)));
    }
}
