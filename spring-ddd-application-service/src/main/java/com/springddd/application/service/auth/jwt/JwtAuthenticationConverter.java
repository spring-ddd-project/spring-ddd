package com.springddd.application.service.auth.jwt;

import com.springddd.application.service.auth.SecurityProperties;
import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.user.UserId;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationConverter implements ServerAuthenticationConverter {

    private final JwtTemplate jwtTemplate;

    private final SecurityProperties securityProperties;

    private final PathPatternParser pathPatternParser = new PathPatternParser();

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        boolean isIgnored = securityProperties.getIgnorePaths().stream()
                .map(pathPatternParser::parse)
                .anyMatch(pattern -> pattern.matches(exchange.getRequest().getPath()));

        if (isIgnored) {
            return Mono.empty();
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (ObjectUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            return Mono.error(new AccessDeniedException("Missing or invalid Authorization header"));
        }

        String token = authHeader.substring(7).trim().replaceAll("\\s+", "");

        Jws<Claims> claims = jwtTemplate.parseToken(token);
        Claims payload = claims.getPayload();
        Long userId = payload.get("userId", Long.class);
        String username = payload.get("username", String.class);
        @SuppressWarnings("unchecked")
        List<String> roles = payload.get("roles", List.class);
        @SuppressWarnings("unchecked")
        List<String> permissions = payload.get("permissions", List.class);
        @SuppressWarnings("unchecked")
        List<Long> menuIds = payload.get("menuIds", List.class);

        AuthUser user = new AuthUser();
        user.setUserId(new UserId(userId));
        user.setUsername(username);
        user.setRoles(roles != null ? roles : List.of());
        user.setPermissions(permissions != null ? permissions : List.of());
        user.setMenuIds(menuIds != null ? menuIds : List.of());
        user.setLockStatus(false);

        return Mono.just(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }
}
