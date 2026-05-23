package com.springddd.application.service.auth;

import com.springddd.application.service.auth.exception.CustomAccessDeniedHandler;
import com.springddd.application.service.auth.exception.CustomAuthenticationEntryPoint;
import com.springddd.application.service.auth.jwt.JwtAuthenticationConverter;
import com.springddd.application.service.auth.jwt.JwtReactiveAuthenticationManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private AuthReactiveUserDetailsService authReactiveUserDetailsService;

    @Mock
    private JwtAuthenticationConverter jwtAuthenticationConverter;

    @Mock
    private JwtReactiveAuthenticationManager jwtAuthenticationManager;

    @Mock
    private AuthorizationManagerConfig authorizationManagerConfig;

    @Mock
    private SecurityProperties securityProperties;

    @Mock
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Mock
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    @DisplayName("authenticationManager 应创建 ReactiveAuthenticationManager")
    void authenticationManager_shouldCreateManager() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        ReactiveAuthenticationManager manager = securityConfig.authenticationManager(encoder);
        assertThat(manager).isNotNull();
    }

    @Test
    @DisplayName("passwordEncoder 应返回 BCryptPasswordEncoder")
    void passwordEncoder_shouldReturnBCrypt() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertThat(encoder).isInstanceOf(BCryptPasswordEncoder.class);
    }

    @Test
    @DisplayName("jwtAuthenticationWebFilter 应创建 AuthenticationWebFilter")
    void jwtAuthenticationWebFilter_shouldCreateFilter() {
        AuthenticationWebFilter filter = securityConfig.jwtAuthenticationWebFilter();
        assertThat(filter).isNotNull();
    }

    @Test
    @DisplayName("securityWebFilterChain 应配置并返回 SecurityWebFilterChain")
    void securityWebFilterChain_shouldConfigureAndReturnChain() {
        when(securityProperties.getIgnorePaths()).thenReturn(List.of("/public/**", "/api/auth/**"));
        ServerHttpSecurity http = ServerHttpSecurity.http();
        SecurityWebFilterChain chain = securityConfig.securityWebFilterChain(http);
        assertThat(chain).isNotNull();
    }
}
