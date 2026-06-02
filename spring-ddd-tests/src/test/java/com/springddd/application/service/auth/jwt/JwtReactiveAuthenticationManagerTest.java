package com.springddd.application.service.auth.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class JwtReactiveAuthenticationManagerTest {

    private final JwtReactiveAuthenticationManager manager = new JwtReactiveAuthenticationManager();

    @Test
    void authenticate_shouldReturnSameAuthentication() {
        Authentication auth = mock(Authentication.class);

        StepVerifier.create(manager.authenticate(auth))
                .expectNext(auth)
                .verifyComplete();
    }
}
