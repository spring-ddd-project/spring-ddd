package com.springddd.application.service.auth;

import com.springddd.application.service.auth.jwt.JwtTemplate;
import com.springddd.domain.auth.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final ReactiveAuthenticationManager authenticationManager;

    private final JwtTemplate jwtTemplate;

    public Mono<String> getToken(String username, String password) {
        UsernamePasswordAuthenticationToken unauthenticated =
                UsernamePasswordAuthenticationToken.unauthenticated(username, password);

        return authenticationManager.authenticate(unauthenticated)
                .map(auth -> {
                    AuthUser user = (AuthUser) auth.getPrincipal();
                    Map<String, Object> map = new HashMap<>();
                    map.put("username", user.getUsername());
                    map.put("permissions", user.getPermissions());
                    return jwtTemplate.generateToken(map);
                });
    }
}

