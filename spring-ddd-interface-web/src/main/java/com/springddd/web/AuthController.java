package com.springddd.web;

import com.springddd.application.service.auth.AuthUserService;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUserService authUserService;

    @PostMapping("/login")
    public Mono<ApiResponse> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        return ApiResponse.ok(authUserService.getToken(username, password));
    }
}
