package com.springddd.application.service.auth;

import com.springddd.application.service.auth.jwt.JwtReactiveAuthenticationManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtReactiveAuthenticationManager Tests")
class JwtReactiveAuthenticationManagerTest {

    @InjectMocks
    private JwtReactiveAuthenticationManager jwtReactiveAuthenticationManager;

    private Authentication testAuthentication;

    @BeforeEach
    void setUp() {
        User principal = new User(
                "testuser",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        testAuthentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                principal,
                "credentials",
                principal.getAuthorities()
        );
    }

    @Test
    @DisplayName("authenticate() should return the same authentication")
    void authenticate_ReturnsSameAuthentication() {
        StepVerifier.create(jwtReactiveAuthenticationManager.authenticate(testAuthentication))
                .assertNext(auth -> {
                    assertThat(auth).isEqualTo(testAuthentication);
                    assertThat(auth.isAuthenticated()).isTrue();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("authenticate() should preserve principal in returned authentication")
    void authenticate_PreservesPrincipal() {
        StepVerifier.create(jwtReactiveAuthenticationManager.authenticate(testAuthentication))
                .assertNext(auth -> {
                    assertThat(auth.getPrincipal()).isEqualTo(testAuthentication.getPrincipal());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("authenticate() should preserve credentials in returned authentication")
    void authenticate_PreservesCredentials() {
        StepVerifier.create(jwtReactiveAuthenticationManager.authenticate(testAuthentication))
                .assertNext(auth -> {
                    assertThat(auth.getCredentials()).isEqualTo("credentials");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("authenticate() should preserve authorities in returned authentication")
    void authenticate_PreservesAuthorities() {
        StepVerifier.create(jwtReactiveAuthenticationManager.authenticate(testAuthentication))
                .assertNext(auth -> {
                    assertThat(auth.getAuthorities())
                            .extracting("authority")
                            .containsExactly("ROLE_USER");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("authenticate() with different authentication types should work")
    void authenticate_WithDifferentAuthTypes_Works() {
        Authentication tokenAuth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                "token",
                null,
                List.of()
        );

        StepVerifier.create(jwtReactiveAuthenticationManager.authenticate(tokenAuth))
                .assertNext(auth -> {
                    assertThat(auth).isEqualTo(tokenAuth);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("authenticate() should return Mono with authentication")
    void authenticate_ReturnsMonoOfAuthentication() {
        Mono<Authentication> result = jwtReactiveAuthenticationManager.authenticate(testAuthentication);

        assertThat(result).isNotNull();
        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
    }
}
