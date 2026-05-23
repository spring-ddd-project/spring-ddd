package com.springddd.application.service.auth.handler;

import com.springddd.application.service.auth.dto.LoginUserQuery;
import com.springddd.domain.auth.AuthUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

class AuthHandlerTest {

    @Test
    @DisplayName("setNext 应设置下一个处理器")
    void setNext_shouldSetNextHandler() {
        AuthHandler handler1 = new TestAuthHandler(Mono.just(new AuthUser()));
        AuthHandler handler2 = new TestAuthHandler(Mono.empty());

        handler1.setNext(handler2);
        assertThat(handler1.next).isSameAs(handler2);
    }

    @Test
    @DisplayName("责任链应依次调用下一个处理器")
    void chain_shouldCallNextHandler() {
        AuthUser user = new AuthUser();
        user.setUsername("test");

        AuthHandler handler1 = new TestAuthHandler(Mono.empty());
        AuthHandler handler2 = new TestAuthHandler(Mono.just(user));
        handler1.setNext(handler2);

        StepVerifier.create(handler1.handle(new LoginUserQuery()))
                .assertNext(result -> assertThat(result.getUsername()).isEqualTo("test"))
                .verifyComplete();
    }

    static class TestAuthHandler extends AuthHandler {
        private final Mono<AuthUser> result;

        TestAuthHandler(Mono<AuthUser> result) {
            this.result = result;
        }

        @Override
        public Mono<AuthUser> handle(LoginUserQuery query) {
            return result.switchIfEmpty(next != null ? next.handle(query) : Mono.empty());
        }
    }
}
