package com.springddd.application.service.auth.jwt;

import com.springddd.application.service.auth.AuthReactiveUserDetailsService;
import com.springddd.application.service.auth.SecurityProperties;
import com.springddd.application.service.auth.cache.UserDetailCacheService;
import com.springddd.domain.auth.AuthUser;
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

@Component
@RequiredArgsConstructor
public class JwtAuthenticationConverter implements ServerAuthenticationConverter {

    private final JwtTemplate jwtTemplate;

    private final SecurityProperties securityProperties;

    private final UserDetailCacheService userDetailCacheService;

    private final AuthReactiveUserDetailsService authReactiveUserDetailsService;

    private final JwtSecret jwtSecret;

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

        return userDetailCacheService.get(userId)
                .switchIfEmpty(loadAndCache(username, userId))
                .map(user -> new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }

    private Mono<AuthUser> loadAndCache(String username, Long userId) {
        return authReactiveUserDetailsService.findByUsername(username)
                .cast(AuthUser.class)
                .flatMap(user -> {
                    user.setPassword(null);
                    return userDetailCacheService.save(user, jwtSecret.getTtl()).then(Mono.just(user));
                })
                .switchIfEmpty(Mono.error(new AccessDeniedException("User not found: " + userId)));
    }
}
