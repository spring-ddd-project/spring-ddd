package com.springddd.application.service.auth;

import com.springddd.application.service.auth.exception.CustomAccessDeniedHandler;
import com.springddd.application.service.auth.exception.CustomAuthenticationEntryPoint;
import com.springddd.application.service.auth.jwt.JwtAuthenticationConverter;
import com.springddd.application.service.auth.jwt.JwtReactiveAuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity
public class SecurityConfig {

    private final AuthReactiveUserDetailsService authReactiveUserDetailsService;

    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    private final JwtReactiveAuthenticationManager jwtAuthenticationManager;

    private final AuthorizationManagerConfig authorizationManagerConfig;

    private final SecurityProperties securityProperties;

    private final CustomAccessDeniedHandler accessDeniedHandler;

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public ReactiveAuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        UserDetailsRepositoryReactiveAuthenticationManager authManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(authReactiveUserDetailsService);
        authManager.setPasswordEncoder(passwordEncoder);
        return authManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationWebFilter jwtAuthenticationWebFilter() {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(jwtAuthenticationManager) {
            @Override
            protected Mono<Void> onAuthenticationSuccess(Authentication authentication,
                                                           WebFilterExchange webFilterExchange) {
                SecurityContextImpl securityContext = new SecurityContextImpl();
                securityContext.setAuthentication(authentication);
                return webFilterExchange.getChain()
                        .filter(webFilterExchange.getExchange())
                        .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
            }
        };
        filter.setServerAuthenticationConverter(jwtAuthenticationConverter);
        return filter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(securityProperties.getIgnorePaths().toArray(new String[0])).permitAll()
                        .anyExchange().access(authorizationManagerConfig)
                )
                .addFilterAt(jwtAuthenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .build();
    }

}
