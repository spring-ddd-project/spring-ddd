package com.springddd.application.service.auth.jwt;

import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.menu.MenuPermission;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationConverter implements ServerAuthenticationConverter {

    private final JwtTemplate jwtTemplate;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (ObjectUtils.isEmpty(token)) {
            return Mono.empty();
        }

        try {
            Jws<Claims> claims = jwtTemplate.parseToken(token);
            String username = claims.getPayload().get("username", String.class);
            List<String> permissions = claims.getPayload().get("permissions", List.class);

            AuthUser user = new AuthUser();
            user.setUsername(username);
            user.setPermissions(permissions.stream().map(MenuPermission::new).toList());

            return Mono.just(
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
            );
        } catch (Exception e) {
            return Mono.empty();
        }
    }
}

