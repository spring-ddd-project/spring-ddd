package com.springddd.application.service.auth;

import com.springddd.application.service.menu.SysMenuQueryService;
import com.springddd.application.service.menu.dto.SysMenuView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthorizationManagerConfigTest {

    @Mock
    private SysMenuQueryService sysMenuQueryService;

    @Mock
    private SecurityProperties securityProperties;

    @Mock
    private ServerWebExchange exchange;

    @Mock
    private ServerHttpRequest request;

    @Mock
    private RequestPath requestPath;

    @Mock
    private AuthorizationContext context;

    private AuthorizationManagerConfig config;

    @BeforeEach
    void setUp() {
        config = new AuthorizationManagerConfig(sysMenuQueryService, securityProperties);
        when(context.getExchange()).thenReturn(exchange);
        when(exchange.getRequest()).thenReturn(request);
        when(request.getPath()).thenReturn(requestPath);
    }

    @Test
    void check_shouldAllow_whenPathInIgnorePaths() {
        when(requestPath.value()).thenReturn("/api/auth/login");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of("/api/auth/login"));

        StepVerifier.create(config.check(Mono.empty(), context))
                .assertNext(decision -> assertTrue(decision.isGranted()))
                .verifyComplete();
    }

    @Test
    void check_shouldAllow_whenPathInTokenOnlyPaths() {
        when(requestPath.value()).thenReturn("/api/token");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of("/api/token"));

        StepVerifier.create(config.check(Mono.empty(), context))
                .assertNext(decision -> assertTrue(decision.isGranted()))
                .verifyComplete();
    }

    @Test
    void check_shouldDeny_whenMenuNotFound() {
        when(requestPath.value()).thenReturn("/api/unknown");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());
        when(sysMenuQueryService.queryByApi("/api/unknown")).thenReturn(Mono.empty());

        StepVerifier.create(config.check(Mono.empty(), context))
                .assertNext(decision -> assertFalse(decision.isGranted()))
                .verifyComplete();
    }

    @Test
    void check_shouldDeny_whenMenuPermissionIsEmpty() {
        when(requestPath.value()).thenReturn("/api/test");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());

        SysMenuView menu = new SysMenuView();
        menu.setPermission("");
        when(sysMenuQueryService.queryByApi("/api/test")).thenReturn(Mono.just(menu));

        StepVerifier.create(config.check(Mono.empty(), context))
                .assertNext(decision -> assertFalse(decision.isGranted()))
                .verifyComplete();
    }

    @Test
    void check_shouldAllow_whenUserHasPermission() {
        when(requestPath.value()).thenReturn("/api/test");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());

        SysMenuView menu = new SysMenuView();
        menu.setPermission("sys:user:list");
        when(sysMenuQueryService.queryByApi("/api/test")).thenReturn(Mono.just(menu));

        Authentication auth = new TestingAuthenticationToken("user", null,
                List.of(new SimpleGrantedAuthority("sys:user:list")));

        StepVerifier.create(config.check(Mono.just(auth), context))
                .assertNext(decision -> assertTrue(decision.isGranted()))
                .verifyComplete();
    }

    @Test
    void check_shouldDeny_whenUserLacksPermission() {
        when(requestPath.value()).thenReturn("/api/test");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());

        SysMenuView menu = new SysMenuView();
        menu.setPermission("sys:user:list");
        when(sysMenuQueryService.queryByApi("/api/test")).thenReturn(Mono.just(menu));

        Authentication auth = new TestingAuthenticationToken("user", null,
                List.of(new SimpleGrantedAuthority("sys:role:list")));

        StepVerifier.create(config.check(Mono.just(auth), context))
                .assertNext(decision -> assertFalse(decision.isGranted()))
                .verifyComplete();
    }

    @Test
    void check_shouldDeny_whenMenuIsNull() {
        when(requestPath.value()).thenReturn("/api/test");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());

        SysMenuView menu = new SysMenuView();
        menu.setPermission(null);
        when(sysMenuQueryService.queryByApi("/api/test")).thenReturn(Mono.just(menu));

        StepVerifier.create(config.check(Mono.empty(), context))
                .assertNext(decision -> assertFalse(decision.isGranted()))
                .verifyComplete();
    }

    @Test
    void check_shouldIterateFullIgnorePaths_whenNoMatch() {
        when(requestPath.value()).thenReturn("/api/other");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of("/api/auth/login", "/api/register"));
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of("/api/token"));
        when(sysMenuQueryService.queryByApi("/api/other")).thenReturn(Mono.empty());

        StepVerifier.create(config.check(Mono.empty(), context))
                .assertNext(decision -> assertFalse(decision.isGranted()))
                .verifyComplete();
    }

    @Test
    void check_shouldAllow_whenPathMatchesSecondIgnorePath() {
        when(requestPath.value()).thenReturn("/api/register");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of("/api/auth/login", "/api/register"));

        StepVerifier.create(config.check(Mono.empty(), context))
                .assertNext(decision -> assertTrue(decision.isGranted()))
                .verifyComplete();
    }

    @Test
    void check_shouldAllow_whenPathMatchesSecondTokenOnlyPath() {
        when(requestPath.value()).thenReturn("/api/refresh");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of("/api/token", "/api/refresh"));

        StepVerifier.create(config.check(Mono.empty(), context))
                .assertNext(decision -> assertTrue(decision.isGranted()))
                .verifyComplete();
    }

    @Test
    void check_shouldDeny_whenNotAuthenticated() {
        when(requestPath.value()).thenReturn("/api/test");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());

        SysMenuView menu = new SysMenuView();
        menu.setPermission("sys:user:list");
        when(sysMenuQueryService.queryByApi("/api/test")).thenReturn(Mono.just(menu));

        Authentication auth = new TestingAuthenticationToken("user", null);
        auth.setAuthenticated(false);

        StepVerifier.create(config.check(Mono.just(auth), context))
                .assertNext(decision -> assertFalse(decision.isGranted()))
                .verifyComplete();
    }

    @Test
    void check_shouldAllow_whenSecondAuthorityMatches() {
        when(requestPath.value()).thenReturn("/api/test");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());

        SysMenuView menu = new SysMenuView();
        menu.setPermission("sys:user:list");
        when(sysMenuQueryService.queryByApi("/api/test")).thenReturn(Mono.just(menu));

        Authentication auth = new TestingAuthenticationToken("user", null,
                List.of(new SimpleGrantedAuthority("sys:role:list"), new SimpleGrantedAuthority("sys:user:list")));

        StepVerifier.create(config.check(Mono.just(auth), context))
                .assertNext(decision -> assertTrue(decision.isGranted()))
                .verifyComplete();
    }

    @Test
    void check_shouldDeny_whenMenuReferenceIsNull() throws Exception {
        when(requestPath.value()).thenReturn("/api/test");
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());

        // Invoke the flatMap lambda directly with null menu to cover the menu==null branch
        Method lambdaMethod = null;
        for (Method m : AuthorizationManagerConfig.class.getDeclaredMethods()) {
            if (m.isSynthetic() && m.getName().startsWith("lambda$check")
                    && Mono.class.isAssignableFrom(m.getReturnType())) {
                lambdaMethod = m;
                break;
            }
        }
        assertNotNull(lambdaMethod, "Lambda method not found");
        lambdaMethod.setAccessible(true);

        Object[] args = new Object[lambdaMethod.getParameterCount()];
        for (int i = 0; i < args.length; i++) {
            Class<?> paramType = lambdaMethod.getParameterTypes()[i];
            if (Mono.class.isAssignableFrom(paramType)) {
                args[i] = Mono.just(new TestingAuthenticationToken("user", null, List.of()));
            } else if (String.class.equals(paramType)) {
                args[i] = "/api/test";
            } else {
                args[i] = null;
            }
        }

        @SuppressWarnings("unchecked")
        Mono<AuthorizationDecision> result = (Mono<AuthorizationDecision>) lambdaMethod.invoke(config, args);
        StepVerifier.create(result)
                .assertNext(decision -> assertFalse(decision.isGranted()))
                .verifyComplete();
    }
}
