package com.springddd.application.service.auth.handler;

import com.springddd.application.service.auth.dto.LoginUserQuery;
import com.springddd.domain.auth.AuthUser;
import reactor.core.publisher.Mono;

public abstract class AuthHandler {
    protected AuthHandler next;

    public void setNext(AuthHandler next) {
        this.next = next;
    }

    public abstract Mono<AuthUser> handle(LoginUserQuery query);
}









