package com.springddd.application.service.auth;

import com.springddd.application.service.auth.exception.CustomAccessDeniedHandler;
import com.springddd.application.service.auth.exception.CustomAuthenticationEntryPoint;
import com.springddd.application.service.auth.jwt.JwtAuthenticationConverter;
import com.springddd.application.service.auth.jwt.JwtReactiveAuthenticationManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private AuthReactiveUserDetailsService authReactiveUserDetailsService;

    @Mock
    private JwtAuthenticationConverter jwtAuthenticationConverter;

    @Mock
    private JwtReactiveAuthenticationManager jwtReactiveAuthenticationManager;

    @Mock
    private AuthorizationManagerConfig authorizationManagerConfig;

    @Mock
    private SecurityProperties securityProperties;

    @Mock
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Mock
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig(
                authReactiveUserDetailsService,
                jwtAuthenticationConverter,
                jwtReactiveAuthenticationManager,
                authorizationManagerConfig,
                securityProperties,
                accessDeniedHandler,
                authenticationEntryPoint
        );
    }

    @Test
    void authenticationManager_shouldReturnReactiveAuthenticationManager() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        ReactiveAuthenticationManager manager = securityConfig.authenticationManager(encoder);
        assertNotNull(manager);
    }

    @Test
    void passwordEncoder_shouldReturnBCryptPasswordEncoder() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void jwtAuthenticationWebFilter_shouldReturnAuthenticationWebFilter() {
        AuthenticationWebFilter filter = securityConfig.jwtAuthenticationWebFilter();
        assertNotNull(filter);
    }

    @Test
    void securityWebFilterChain_shouldReturnFilterChain() {
        org.springframework.security.config.web.server.ServerHttpSecurity http =
                org.springframework.security.config.web.server.ServerHttpSecurity.http();
        when(securityProperties.getIgnorePaths()).thenReturn(List.of("/api/auth/login"));

        SecurityWebFilterChain chain = securityConfig.securityWebFilterChain(http);
        assertNotNull(chain);
    }
}
