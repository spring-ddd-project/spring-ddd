package com.springddd.web;

import com.springddd.application.service.auth.AuthUserService;
import com.springddd.application.service.auth.dto.LoginUserView;
import com.springddd.application.service.auth.dto.UserInfoView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

class AuthControllerTest {

    private WebTestClient webTestClient;
    private AuthUserService authUserService;

    @BeforeEach
    void setUp() {
        authUserService = mock(AuthUserService.class);
        AuthController controller = new AuthController(authUserService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @DisplayName("POST /auth/login 应返回登录令牌")
    void login_shouldReturnToken() {
        LoginUserView view = new LoginUserView();
        view.setAccessToken("test-token-123");
        when(authUserService.getToken(any())).thenReturn(Mono.just(view));

        webTestClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"username\":\"admin\",\"password\":\"123456\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data.accessToken").isEqualTo("test-token-123");
    }

    @Test
    @DisplayName("POST /auth/login 异常时应返回500错误")
    void login_shouldReturnError_whenException() {
        when(authUserService.getToken(any())).thenReturn(Mono.error(new RuntimeException("invalid credentials")));

        webTestClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"username\":\"admin\",\"password\":\"wrong\"}")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("POST /auth/logout 应清除缓存成功")
    void logout_shouldSuccess() {
        when(authUserService.clearCache()).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/auth/logout")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /auth/user/info 应返回用户信息")
    void userInfo_shouldReturnUserInfo() {
        UserInfoView view = new UserInfoView();
        view.setRealName("Admin");
        view.setRoles(List.of("admin"));
        when(authUserService.getUserInfo()).thenReturn(Mono.just(view));

        webTestClient.post()
                .uri("/auth/user/info")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data.realName").isEqualTo("Admin")
                .jsonPath("$.data.roles[0]").isEqualTo("admin");
    }

    @Test
    @DisplayName("POST /auth/codes 应返回权限列表")
    void codes_shouldReturnPermissions() {
        when(authUserService.getUserPermissions()).thenReturn(Mono.just(List.of("sys:user:list", "sys:role:list")));

        webTestClient.post()
                .uri("/auth/codes")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data[0]").isEqualTo("sys:user:list");
    }
}
