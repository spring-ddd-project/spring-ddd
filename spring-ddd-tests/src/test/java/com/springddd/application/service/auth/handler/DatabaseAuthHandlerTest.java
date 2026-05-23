package com.springddd.application.service.auth.handler;

import com.springddd.application.service.auth.dto.LoginUserQuery;
import com.springddd.domain.auth.AuthUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DatabaseAuthHandlerTest {

    @Mock
    private ReactiveAuthenticationManager authenticationManager;

    @InjectMocks
    private DatabaseAuthHandler handler;

    @Test
    @DisplayName("handle 应通过 authenticationManager 认证并返回 AuthUser")
    void handle_shouldAuthenticateAndReturnAuthUser() {
        LoginUserQuery query = new LoginUserQuery();
        query.setUsername("admin");
        query.setPassword("123456");

        AuthUser user = new AuthUser();
        user.setUsername("admin");

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(Mono.just(auth));

        StepVerifier.create(handler.handle(query))
                .assertNext(result -> assertThat(result.getUsername()).isEqualTo("admin"))
                .verifyComplete();
    }

    @Test
    @DisplayName("handle 当认证失败且无下一个处理器时应返回 empty")
    void handle_whenAuthFailsAndNoNext_shouldReturnEmpty() {
        LoginUserQuery query = new LoginUserQuery();
        query.setUsername("admin");
        query.setPassword("wrong");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(Mono.empty());

        StepVerifier.create(handler.handle(query))
                .verifyComplete();
    }

    @Test
    @DisplayName("handle 当认证失败且有下一个处理器时应委托给下一个处理器")
    void handle_whenAuthFailsWithNext_shouldDelegateToNext() {
        LoginUserQuery query = new LoginUserQuery();
        query.setUsername("admin");
        query.setPassword("wrong");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(Mono.empty());

        AuthUser fallbackUser = new AuthUser();
        fallbackUser.setUsername("fallback");

        AuthHandler nextHandler = new AuthHandler() {
            @Override
            public Mono<AuthUser> handle(LoginUserQuery query) {
                return Mono.just(fallbackUser);
            }
        };
        handler.setNext(nextHandler);

        StepVerifier.create(handler.handle(query))
                .assertNext(result -> assertThat(result.getUsername()).isEqualTo("fallback"))
                .verifyComplete();
    }
}
