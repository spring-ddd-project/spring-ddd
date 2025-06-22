package com.springddd.web;

import com.springddd.application.service.auth.AuthUserService;
import com.springddd.application.service.auth.dto.LoginUserQuery;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUserService authUserService;

    @PostMapping("/login")
    public Mono<ApiResponse> login(@RequestBody @Validated Mono<LoginUserQuery> query) {
        return ApiResponse.validated(query, authUserService::getToken);
    }

    @PostMapping("/user/info")
    public Mono<ApiResponse> userInfo() {
        return ApiResponse.ok(authUserService.getUserInfo());
    }

    @PostMapping("/codes")
    public Mono<ApiResponse> codes() {
        return ApiResponse.ok(authUserService.getUserPermissions());
    }
}
