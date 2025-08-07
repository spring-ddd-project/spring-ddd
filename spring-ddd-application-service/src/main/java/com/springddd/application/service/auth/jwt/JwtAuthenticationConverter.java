package com.springddd.application.service.auth.jwt;

import com.springddd.application.service.auth.SecurityProperties;
import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.menu.MenuPermission;
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

        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (ObjectUtils.isEmpty(token)) {
            return Mono.error(new AccessDeniedException("Missing Authorization header"));
        }

        try {
            Jws<Claims> claims = jwtTemplate.parseToken(token);
            String username = claims.getPayload().get("username", String.class);
            List<String> permissions = claims.getPayload().get("permissions", List.class);

            AuthUser user = new AuthUser();
            user.setUsername(username);
            user.setPermissions(
                    permissions.stream()
                            .map(MenuPermission::new)
                            .toList()
            );

            return Mono.just(
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
            );
        } catch (Exception e) {
            return Mono.error(new AccessDeniedException(e.getMessage()));
        }
    }

}

