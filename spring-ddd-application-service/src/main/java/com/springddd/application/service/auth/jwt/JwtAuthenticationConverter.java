package com.springddd.application.service.auth.jwt;

import com.springddd.application.service.auth.SecurityProperties;
import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationConverter implements ServerAuthenticationConverter {

    private final JwtTemplate jwtTemplate;

    private final SecurityProperties securityProperties;

    private final PathPatternParser pathPatternParser = new PathPatternParser();

    private final ReactiveRedisCacheHelper reactiveRedisCacheHelper;

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
            log.error("\n#JwtAuthenticationConverter#[Missing or invalid Authorization header]: {}", authHeader);
            return Mono.error(new AccessDeniedException("Missing or invalid Authorization header"));
        }

        // Extract the token part by removing the "Bearer" prefix and trimming any extra whitespace.
        String token = authHeader.substring(7).trim().replaceAll("\\s+", "");

        boolean isTokenOnly = securityProperties.getTokenOnlyPaths().stream()
                .map(pathPatternParser::parse)
                .anyMatch(pattern -> pattern.matches(exchange.getRequest().getPath()));

        Jws<Claims> claims = jwtTemplate.parseToken(token);
        Long userId = claims.getPayload().get("userId", Long.class);

        return reactiveRedisCacheHelper.getCache("user:" + userId + ":token", String.class)
                .switchIfEmpty(Mono.error(new AccessDeniedException("Request has expired")))
                .flatMap(cachedToken -> {
                    if (!cachedToken.equals(token)) {
                        log.error("\n#JwtAuthenticationConverter#[token does not exist]: {}", token);
                        return Mono.error(new AccessDeniedException("Invalid Request"));
                    }

                    if (isTokenOnly) {
                        return Mono.empty();
                    }

                    // Token matches cache, continue with parsing
                    try {
                        String userCacheKey = "user:" + userId + ":detail";
                        return reactiveRedisCacheHelper.getCache(userCacheKey, AuthUser.class)
                                .switchIfEmpty(Mono.error(new AccessDeniedException("User detail not found in cache")))
                                .map(user -> new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

                    } catch (Exception e) {
                        log.error("\n#JwtAuthenticationConverter#[Invalid token]: {}", e.getMessage());
                        return Mono.error(new AccessDeniedException("Invalid token: " + e.getMessage()));
                    }
                });
    }


}

