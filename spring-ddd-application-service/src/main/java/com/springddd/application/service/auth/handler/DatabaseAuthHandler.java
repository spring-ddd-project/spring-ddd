package com.springddd.application.service.auth.handler;

import com.springddd.application.service.auth.dto.LoginUserQuery;
import com.springddd.domain.auth.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DatabaseAuthHandler extends AuthHandler {
    private final ReactiveAuthenticationManager authenticationManager;

    @Override
    public Mono<AuthUser> handle(LoginUserQuery query) {
        UsernamePasswordAuthenticationToken unauthenticated =
                UsernamePasswordAuthenticationToken.unauthenticated(query.getUsername(), query.getPassword());

        return authenticationManager.authenticate(unauthenticated)
                .map(auth -> (AuthUser) auth.getPrincipal())
                .switchIfEmpty(next != null ? next.handle(query) : Mono.empty());
    }
}



























