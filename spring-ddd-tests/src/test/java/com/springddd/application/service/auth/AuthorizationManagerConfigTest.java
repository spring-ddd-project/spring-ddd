package com.springddd.application.service.auth;

import com.springddd.application.service.menu.SysMenuQueryService;
import com.springddd.application.service.menu.dto.SysMenuView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationManagerConfigTest {

    @Mock
    private SysMenuQueryService sysMenuQueryService;

    @Mock
    private SecurityProperties securityProperties;

    @Mock
    private AuthorizationContext authorizationContext;

    @Mock
    private ServerWebExchange exchange;

    private AuthorizationManagerConfig authorizationManagerConfig;

    @BeforeEach
    void setUp() {
        authorizationManagerConfig = new AuthorizationManagerConfig(sysMenuQueryService, securityProperties);
    }

    @Test
    void shouldReturnTrueWhenPathIsInIgnorePaths() {
        when(authorizationContext.getExchange()).thenReturn(exchange);
        when(exchange.getRequest()).thenReturn(mock(org.springframework.http.server.reactive.ServerHttpRequest.class));
        when(exchange.getRequest().getPath()).thenReturn(mock(org.springframework.web.server.adapter.HttpRequest.class));
        when(exchange.getRequest().getPath().value()).thenReturn("/public/api");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of("/public/api"));

        Mono<org.springframework.security.authorization.AuthorizationDecision> result = 
                authorizationManagerConfig.check(Mono.just(mock(Authentication.class)), authorizationContext);

        StepVerifier.create(result)
                .assertNext(decision -> assertTrue(decision.isGranted()))
                .verifyComplete();
    }

    @Test
    void shouldReturnTrueWhenPathIsInTokenOnlyPaths() {
        when(authorizationContext.getExchange()).thenReturn(exchange);
        when(exchange.getRequest()).thenReturn(mock(org.springframework.http.server.reactive.ServerHttpRequest.class));
        when(exchange.getRequest().getPath()).thenReturn(mock(org.springframework.web.server.adapter.HttpRequest.class));
        when(exchange.getRequest().getPath().value()).thenReturn("/token/only/path");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of("/token/only/path"));

        Mono<org.springframework.security.authorization.AuthorizationDecision> result = 
                authorizationManagerConfig.check(Mono.just(mock(Authentication.class)), authorizationContext);

        StepVerifier.create(result)
                .assertNext(decision -> assertTrue(decision.isGranted()))
                .verifyComplete();
    }

    @Test
    void shouldReturnFalseWhenNoMenuFound() {
        when(authorizationContext.getExchange()).thenReturn(exchange);
        when(exchange.getRequest()).thenReturn(mock(org.springframework.http.server.reactive.ServerHttpRequest.class));
        when(exchange.getRequest().getPath()).thenReturn(mock(org.springframework.web.server.adapter.HttpRequest.class));
        when(exchange.getRequest().getPath().value()).thenReturn("/some/path");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());
        when(sysMenuQueryService.queryByApi(anyString())).thenReturn(Mono.empty());

        Mono<org.springframework.security.authorization.AuthorizationDecision> result = 
                authorizationManagerConfig.check(Mono.just(mock(Authentication.class)), authorizationContext);

        StepVerifier.create(result)
                .assertNext(decision -> assertFalse(decision.isGranted()))
                .verifyComplete();
    }

    @Test
    void shouldReturnFalseWhenMenuPermissionIsEmpty() {
        when(authorizationContext.getExchange()).thenReturn(exchange);
        when(exchange.getRequest()).thenReturn(mock(org.springframework.http.server.reactive.ServerHttpRequest.class));
        when(exchange.getRequest().getPath()).thenReturn(mock(org.springframework.web.server.adapter.HttpRequest.class));
        when(exchange.getRequest().getPath().value()).thenReturn("/some/path");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());
        
        SysMenuView menuView = new SysMenuView();
        menuView.setPermission("");
        when(sysMenuQueryService.queryByApi(anyString())).thenReturn(Mono.just(menuView));

        TestingAuthenticationToken auth = new TestingAuthenticationToken("user", "pass", List.of());
        Mono<org.springframework.security.authorization.AuthorizationDecision> result = 
                authorizationManagerConfig.check(Mono.just(auth), authorizationContext);

        StepVerifier.create(result)
                .assertNext(decision -> assertFalse(decision.isGranted()))
                .verifyComplete();
    }

    @Test
    void shouldReturnTrueWhenUserHasRequiredPermission() {
        when(authorizationContext.getExchange()).thenReturn(exchange);
        when(exchange.getRequest()).thenReturn(mock(org.springframework.http.server.reactive.ServerHttpRequest.class));
        when(exchange.getRequest().getPath()).thenReturn(mock(org.springframework.web.server.adapter.HttpRequest.class));
        when(exchange.getRequest().getPath().value()).thenReturn("/some/path");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());
        
        SysMenuView menuView = new SysMenuView();
        menuView.setPermission("admin:menu:read");
        when(sysMenuQueryService.queryByApi(anyString())).thenReturn(Mono.just(menuView));

        TestingAuthenticationToken auth = new TestingAuthenticationToken(
                "user", "pass", List.of(new SimpleGrantedAuthority("admin:menu:read")));
        
        Mono<org.springframework.security.authorization.AuthorizationDecision> result = 
                authorizationManagerConfig.check(Mono.just(auth), authorizationContext);

        StepVerifier.create(result)
                .assertNext(decision -> assertTrue(decision.isGranted()))
                .verifyComplete();
    }

    @Test
    void shouldReturnFalseWhenUserLacksRequiredPermission() {
        when(authorizationContext.getExchange()).thenReturn(exchange);
        when(exchange.getRequest()).thenReturn(mock(org.springframework.http.server.reactive.ServerHttpRequest.class));
        when(exchange.getRequest().getPath()).thenReturn(mock(org.springframework.web.server.adapter.HttpRequest.class));
        when(exchange.getRequest().getPath().value()).thenReturn("/some/path");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());
        
        SysMenuView menuView = new SysMenuView();
        menuView.setPermission("admin:menu:write");
        when(sysMenuQueryService.queryByApi(anyString())).thenReturn(Mono.just(menuView));

        TestingAuthenticationToken auth = new TestingAuthenticationToken(
                "user", "pass", List.of(new SimpleGrantedAuthority("admin:menu:read")));
        
        Mono<org.springframework.security.authorization.AuthorizationDecision> result = 
                authorizationManagerConfig.check(Mono.just(auth), authorizationContext);

        StepVerifier.create(result)
                .assertNext(decision -> assertFalse(decision.isGranted()))
                .verifyComplete();
    }
}
