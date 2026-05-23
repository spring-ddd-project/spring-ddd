package com.springddd.application.service.auth;

import com.springddd.application.service.menu.SysMenuQueryService;
import com.springddd.application.service.menu.dto.SysMenuView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class AuthorizationManagerConfigTest {

    @Mock
    private SysMenuQueryService sysMenuQueryService;

    @Mock
    private SecurityProperties securityProperties;

    @InjectMocks
    private AuthorizationManagerConfig config;

    @Test
    @DisplayName("忽略路径应直接放行")
    void check_whenIgnorePath_shouldPermit() {
        when(securityProperties.getIgnorePaths()).thenReturn(List.of("/auth/login"));
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());

        MockServerHttpRequest request = MockServerHttpRequest.method(HttpMethod.POST, "/auth/login").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        AuthorizationContext context = new AuthorizationContext(exchange);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("user", null, Collections.emptyList());

        StepVerifier.create(config.check(Mono.just(auth), context))
                .assertNext(decision -> assertThat(decision.isGranted()).isTrue())
                .verifyComplete();
    }

    @Test
    @DisplayName("tokenOnly 路径应直接放行")
    void check_whenTokenOnlyPath_shouldPermit() {
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of("/auth/info"));

        MockServerHttpRequest request = MockServerHttpRequest.method(HttpMethod.POST, "/auth/info").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        AuthorizationContext context = new AuthorizationContext(exchange);

        StepVerifier.create(config.check(Mono.empty(), context))
                .assertNext(decision -> assertThat(decision.isGranted()).isTrue())
                .verifyComplete();
    }

    @Test
    @DisplayName("有权限时应放行")
    void check_whenHasPermission_shouldPermit() {
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());

        SysMenuView menu = new SysMenuView();
        menu.setPermission("sys:user:list");
        when(sysMenuQueryService.queryByApi("/sys/user/index")).thenReturn(Mono.just(menu));

        MockServerHttpRequest request = MockServerHttpRequest.method(HttpMethod.POST, "/sys/user/index").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        AuthorizationContext context = new AuthorizationContext(exchange);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "user", null, List.of(new SimpleGrantedAuthority("sys:user:list")));

        StepVerifier.create(config.check(Mono.just(auth), context))
                .assertNext(decision -> assertThat(decision.isGranted()).isTrue())
                .verifyComplete();
    }

    @Test
    @DisplayName("无权限时应拒绝")
    void check_whenNoPermission_shouldDeny() {
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());

        SysMenuView menu = new SysMenuView();
        menu.setPermission("sys:user:list");
        when(sysMenuQueryService.queryByApi("/sys/user/index")).thenReturn(Mono.just(menu));

        MockServerHttpRequest request = MockServerHttpRequest.method(HttpMethod.POST, "/sys/user/index").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        AuthorizationContext context = new AuthorizationContext(exchange);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "user", null, List.of(new SimpleGrantedAuthority("sys:role:list")));

        StepVerifier.create(config.check(Mono.just(auth), context))
                .assertNext(decision -> assertThat(decision.isGranted()).isFalse())
                .verifyComplete();
    }

    @Test
    @DisplayName("未找到菜单时应拒绝")
    void check_whenMenuNotFound_shouldDeny() {
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());
        when(sysMenuQueryService.queryByApi("/unknown")).thenReturn(Mono.empty());

        MockServerHttpRequest request = MockServerHttpRequest.method(HttpMethod.POST, "/unknown").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        AuthorizationContext context = new AuthorizationContext(exchange);

        StepVerifier.create(config.check(Mono.empty(), context))
                .assertNext(decision -> assertThat(decision.isGranted()).isFalse())
                .verifyComplete();
    }

    @Test
    @DisplayName("菜单为 null 时应拒绝")
    void check_whenMenuIsNull_shouldDeny() {
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());
        when(sysMenuQueryService.queryByApi("/sys/user/index")).thenReturn(Mono.empty());

        MockServerHttpRequest request = MockServerHttpRequest.method(HttpMethod.POST, "/sys/user/index").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        AuthorizationContext context = new AuthorizationContext(exchange);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "user", null, List.of(new SimpleGrantedAuthority("sys:user:list")));

        StepVerifier.create(config.check(Mono.just(auth), context))
                .assertNext(decision -> assertThat(decision.isGranted()).isFalse())
                .verifyComplete();
    }

    @Test
    @DisplayName("菜单权限为空时应拒绝")
    void check_whenMenuPermissionIsEmpty_shouldDeny() {
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());

        SysMenuView menu = new SysMenuView();
        menu.setPermission("");
        when(sysMenuQueryService.queryByApi("/sys/user/index")).thenReturn(Mono.just(menu));

        MockServerHttpRequest request = MockServerHttpRequest.method(HttpMethod.POST, "/sys/user/index").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        AuthorizationContext context = new AuthorizationContext(exchange);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "user", null, List.of(new SimpleGrantedAuthority("sys:user:list")));

        StepVerifier.create(config.check(Mono.just(auth), context))
                .assertNext(decision -> assertThat(decision.isGranted()).isFalse())
                .verifyComplete();
    }

    @Test
    @DisplayName("未认证时应拒绝")
    void check_whenNotAuthenticated_shouldDeny() {
        when(securityProperties.getIgnorePaths()).thenReturn(List.of());
        when(securityProperties.getTokenOnlyPaths()).thenReturn(List.of());

        SysMenuView menu = new SysMenuView();
        menu.setPermission("sys:user:list");
        when(sysMenuQueryService.queryByApi("/sys/user/index")).thenReturn(Mono.just(menu));

        MockServerHttpRequest request = MockServerHttpRequest.method(HttpMethod.POST, "/sys/user/index").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        AuthorizationContext context = new AuthorizationContext(exchange);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "user", null, List.of(new SimpleGrantedAuthority("sys:user:list")));
        auth.setAuthenticated(false);

        StepVerifier.create(config.check(Mono.just(auth), context))
                .assertNext(decision -> assertThat(decision.isGranted()).isFalse())
                .verifyComplete();
    }
}
