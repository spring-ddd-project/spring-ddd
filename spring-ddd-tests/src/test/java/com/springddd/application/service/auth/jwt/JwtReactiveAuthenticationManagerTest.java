package com.springddd.application.service.auth.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JwtReactiveAuthenticationManagerTest {

    @InjectMocks
    private JwtReactiveAuthenticationManager manager;

    @Test
    @DisplayName("authenticate 应直接返回传入的 authentication")
    void authenticate_shouldReturnAuthentication() {
        Authentication auth = new UsernamePasswordAuthenticationToken("user", "pass");

        StepVerifier.create(manager.authenticate(auth))
                .assertNext(result -> assertThat(result).isSameAs(auth))
                .verifyComplete();
    }
}
