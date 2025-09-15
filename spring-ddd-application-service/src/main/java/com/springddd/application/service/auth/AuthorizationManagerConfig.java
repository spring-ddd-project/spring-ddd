package com.springddd.application.service.auth;

import com.springddd.application.service.menu.SysMenuQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationManagerConfig implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final SysMenuQueryService sysMenuQueryService;

    private final SecurityProperties securityProperties;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authenticationMono, AuthorizationContext context) {
        ServerWebExchange exchange = context.getExchange();
        String path = exchange.getRequest().getPath().value();

        List<String> ignorePaths = securityProperties.getIgnorePaths();
        for (String ignorePath : ignorePaths) {
            if (ignorePath.equals(path)) {
                return Mono.just(new AuthorizationDecision(true));
            }
        }

        List<String> tokenOnlyPaths = securityProperties.getTokenOnlyPaths();
        for (String tokenOnlyPath : tokenOnlyPaths) {
            if (tokenOnlyPath.equals(path)) {
                return Mono.just(new AuthorizationDecision(true));
            }
        }

        return sysMenuQueryService.queryByComponent(path)
                .flatMap(menu -> {
                    if (menu == null || ObjectUtils.isEmpty(menu.getPermission())) {
                        log.error("\n#AuthorizationManagerConfig#[Menu permission is empty]:{}", menu);
                        return Mono.just(new AuthorizationDecision(false));
                    }

                    String requiredPermission = menu.getPermission();

                    return authenticationMono
                            .filter(Authentication::isAuthenticated)
                            .map(auth -> {
                                Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
                                for (GrantedAuthority authority : authorities) {
                                    if (authority.getAuthority().equals(requiredPermission)) {
                                        return new AuthorizationDecision(true);
                                    }
                                }
                                log.error("\n#AuthorizationManagerConfig#[User authorities is empty]:{}", auth);
                                return new AuthorizationDecision(false);
                            });
                }).switchIfEmpty(Mono.fromSupplier(() -> {
                    log.error("\n#AuthorizationManagerConfig#[No menu found for path]:{}", path);
                    return new AuthorizationDecision(false);
                }));
    }
}