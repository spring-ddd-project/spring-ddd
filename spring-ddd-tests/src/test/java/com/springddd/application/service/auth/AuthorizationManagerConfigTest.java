package com.springddd.application.service.auth;

import com.springddd.application.service.menu.SysMenuQueryService;
import com.springddd.application.service.menu.dto.SysMenuView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizationManagerConfigTest {

    @Mock
    private SysMenuQueryService sysMenuQueryService;

    @Mock
    private SecurityProperties securityProperties;

    @Mock
    private AuthorizationContext authorizationContext;

    private AuthorizationManagerConfig authorizationManagerConfig;

    @BeforeEach
    void setUp() {
        authorizationManagerConfig = new AuthorizationManagerConfig(sysMenuQueryService, securityProperties);
    }

    @Test
    void check_shouldReturnDenied_whenMenuNotFound() {
        org.springframework.web.server.ServerWebExchange exchange = mock(org.springframework.web.server.ServerWebExchange.class);
        org.springframework.http.server.reactive.ServerHttpRequest request = mock(org.springframework.http.server.reactive.ServerHttpRequest.class);

        when(authorizationContext.getExchange()).thenReturn(exchange);
        when(exchange.getRequest()).thenReturn(request);
        when(request.getPath()).thenReturn(null);
        when(securityProperties.getIgnorePaths()).thenReturn(Collections.emptyList());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(Collections.emptyList());
        when(sysMenuQueryService.queryByApi(any())).thenReturn(Mono.empty());

        Mono<AuthorizationDecision> result = authorizationManagerConfig.check(
                Mono.just(mock(Authentication.class)), authorizationContext);

        StepVerifier.create(result)
                .expectNextMatches(decision -> !decision.isGranted())
                .verifyComplete();
    }

    @Test
    void check_shouldReturnDenied_whenMenuPermissionIsEmpty() {
        SysMenuView menuView = new SysMenuView();
        menuView.setPermission(null);

        org.springframework.web.server.ServerWebExchange exchange = mock(org.springframework.web.server.ServerWebExchange.class);
        org.springframework.http.server.reactive.ServerHttpRequest request = mock(org.springframework.http.server.reactive.ServerHttpRequest.class);

        when(authorizationContext.getExchange()).thenReturn(exchange);
        when(exchange.getRequest()).thenReturn(request);
        when(request.getPath()).thenReturn(null);
        when(securityProperties.getIgnorePaths()).thenReturn(Collections.emptyList());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(Collections.emptyList());
        when(sysMenuQueryService.queryByApi(any())).thenReturn(Mono.just(menuView));

        Mono<AuthorizationDecision> result = authorizationManagerConfig.check(
                Mono.just(mock(Authentication.class)), authorizationContext);

        StepVerifier.create(result)
                .expectNextMatches(decision -> !decision.isGranted())
                .verifyComplete();
    }

    @Test
    void check_shouldReturnDenied_whenUserLacksRequiredPermission() {
        SysMenuView menuView = new SysMenuView();
        menuView.setPermission("test:view");

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("other:permission");
        Collection<GrantedAuthority> authorities = Collections.singletonList(authority);

        Authentication authentication = mock(Authentication.class);

        org.springframework.web.server.ServerWebExchange exchange = mock(org.springframework.web.server.ServerWebExchange.class);
        org.springframework.http.server.reactive.ServerHttpRequest request = mock(org.springframework.http.server.reactive.ServerHttpRequest.class);

        when(authorizationContext.getExchange()).thenReturn(exchange);
        when(exchange.getRequest()).thenReturn(request);
        when(request.getPath()).thenReturn(null);
        when(securityProperties.getIgnorePaths()).thenReturn(Collections.emptyList());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(Collections.emptyList());
        when(sysMenuQueryService.queryByApi(any())).thenReturn(Mono.just(menuView));
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        Mono<AuthorizationDecision> result = authorizationManagerConfig.check(
                Mono.just(authentication), authorizationContext);

        StepVerifier.create(result)
                .expectNextMatches(decision -> !decision.isGranted())
                .verifyComplete();
    }
}
