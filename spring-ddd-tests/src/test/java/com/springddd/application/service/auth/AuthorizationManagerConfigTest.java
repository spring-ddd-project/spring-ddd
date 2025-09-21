package com.springddd.application.service.auth;

import com.springddd.application.service.menu.SysMenuQueryService;
import com.springddd.application.service.menu.dto.SysMenuView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationManagerConfigTest {

    @Mock
    private SysMenuQueryService sysMenuQueryService;

    @Mock
    private SecurityProperties securityProperties;

    @Mock
    private AuthorizationContext authorizationContext;

    @InjectMocks
    private AuthorizationManagerConfig authorizationManagerConfig;

    private Authentication authentication;
    private SysMenuView mockMenuView;

    @BeforeEach
    void setUp() {
        mockMenuView = new SysMenuView();
        mockMenuView.setId(1L);
        mockMenuView.setPermission("user:read");

        authentication = new TestingAuthenticationToken(
                "testuser", "password",
                Arrays.asList(new SimpleGrantedAuthority("user:read"))
        );
    }

    private ServerWebExchange createExchange(String path) {
        MockServerHttpRequest request = MockServerHttpRequest.get(path).build();
        return MockServerWebExchange.from(request);
    }

    @Test
    void check_shouldAllowIgnorePath() {
        ServerWebExchange exchange = createExchange("/public/api");
        when(authorizationContext.getExchange()).thenReturn(exchange);

        when(securityProperties.getIgnorePaths()).thenReturn(Arrays.asList("/public/api"));

        Mono<AuthorizationDecision> result = authorizationManagerConfig.check(
                Mono.just(authentication), authorizationContext
        );

        StepVerifier.create(result)
                .assertNext(decision -> {
                    assertNotNull(decision);
                    assertTrue(decision.isGranted());
                })
                .verifyComplete();
    }

    @Test
    void check_shouldAllowTokenOnlyPath() {
        ServerWebExchange exchange = createExchange("/token/api");
        when(authorizationContext.getExchange()).thenReturn(exchange);

        when(securityProperties.getIgnorePaths()).thenReturn(Collections.emptyList());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(Arrays.asList("/token/api"));

        Mono<AuthorizationDecision> result = authorizationManagerConfig.check(
                Mono.just(authentication), authorizationContext
        );

        StepVerifier.create(result)
                .assertNext(decision -> {
                    assertNotNull(decision);
                    assertTrue(decision.isGranted());
                })
                .verifyComplete();
    }

    @Test
    void check_shouldDenyWhenMenuNotFound() {
        ServerWebExchange exchange = createExchange("/secured/api");
        when(authorizationContext.getExchange()).thenReturn(exchange);

        when(securityProperties.getIgnorePaths()).thenReturn(Collections.emptyList());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(Collections.emptyList());
        when(sysMenuQueryService.queryByApi("/secured/api")).thenReturn(Mono.empty());

        Mono<AuthorizationDecision> result = authorizationManagerConfig.check(
                Mono.just(authentication), authorizationContext
        );

        StepVerifier.create(result)
                .assertNext(decision -> {
                    assertNotNull(decision);
                    assertFalse(decision.isGranted());
                })
                .verifyComplete();
    }

    @Test
    void check_shouldDenyWhenMenuPermissionIsEmpty() {
        mockMenuView.setPermission(null);
        ServerWebExchange exchange = createExchange("/secured/api");
        when(authorizationContext.getExchange()).thenReturn(exchange);

        when(securityProperties.getIgnorePaths()).thenReturn(Collections.emptyList());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(Collections.emptyList());
        when(sysMenuQueryService.queryByApi("/secured/api")).thenReturn(Mono.just(mockMenuView));

        Mono<AuthorizationDecision> result = authorizationManagerConfig.check(
                Mono.just(authentication), authorizationContext
        );

        StepVerifier.create(result)
                .assertNext(decision -> {
                    assertNotNull(decision);
                    assertFalse(decision.isGranted());
                })
                .verifyComplete();
    }

    @Test
    void check_shouldAllowWhenAuthorityMatchesPermission() {
        ServerWebExchange exchange = createExchange("/secured/api");
        when(authorizationContext.getExchange()).thenReturn(exchange);

        when(securityProperties.getIgnorePaths()).thenReturn(Collections.emptyList());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(Collections.emptyList());
        when(sysMenuQueryService.queryByApi("/secured/api")).thenReturn(Mono.just(mockMenuView));

        Mono<AuthorizationDecision> result = authorizationManagerConfig.check(
                Mono.just(authentication), authorizationContext
        );

        StepVerifier.create(result)
                .assertNext(decision -> {
                    assertNotNull(decision);
                    assertTrue(decision.isGranted());
                })
                .verifyComplete();
    }

    @Test
    void check_shouldDenyWhenAuthorityDoesNotMatchPermission() {
        TestingAuthenticationToken authWithoutPermission = new TestingAuthenticationToken(
                "testuser", "password",
                Arrays.asList(new SimpleGrantedAuthority("other:permission"))
        );

        ServerWebExchange exchange = createExchange("/secured/api");
        when(authorizationContext.getExchange()).thenReturn(exchange);

        when(securityProperties.getIgnorePaths()).thenReturn(Collections.emptyList());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(Collections.emptyList());
        when(sysMenuQueryService.queryByApi("/secured/api")).thenReturn(Mono.just(mockMenuView));

        Mono<AuthorizationDecision> result = authorizationManagerConfig.check(
                Mono.just(authWithoutPermission), authorizationContext
        );

        StepVerifier.create(result)
                .assertNext(decision -> {
                    assertNotNull(decision);
                    assertFalse(decision.isGranted());
                })
                .verifyComplete();
    }

    @Test
    void check_shouldDenyWhenNotAuthenticated() {
        TestingAuthenticationToken unauthenticated = new TestingAuthenticationToken(
                "testuser", "password", Collections.emptyList()
        );

        ServerWebExchange exchange = createExchange("/secured/api");
        when(authorizationContext.getExchange()).thenReturn(exchange);

        when(securityProperties.getIgnorePaths()).thenReturn(Collections.emptyList());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(Collections.emptyList());
        when(sysMenuQueryService.queryByApi("/secured/api")).thenReturn(Mono.just(mockMenuView));

        Mono<AuthorizationDecision> result = authorizationManagerConfig.check(
                Mono.just(unauthenticated), authorizationContext
        );

        StepVerifier.create(result)
                .assertNext(decision -> {
                    assertNotNull(decision);
                    assertFalse(decision.isGranted());
                })
                .verifyComplete();
    }
}
