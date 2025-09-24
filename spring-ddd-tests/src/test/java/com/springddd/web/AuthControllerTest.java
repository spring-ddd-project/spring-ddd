package com.springddd.web;

import com.springddd.application.service.auth.AuthUserService;
import com.springddd.application.service.auth.dto.LoginUserQuery;
import com.springddd.application.service.auth.dto.LoginUserView;
import com.springddd.application.service.auth.dto.UserInfoView;
import com.springddd.domain.util.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthUserService authUserService;

    @Test
    void login_shouldReturnOk() {
        LoginUserView view = new LoginUserView();
        view.setToken("test-token");

        when(authUserService.getToken(any(Mono.class))).thenReturn(Mono.just(view));

        webTestClient.post().uri("/auth/login")
                .bodyValue(new LoginUserQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void logout_shouldReturnOk() {
        when(authUserService.clearCache()).thenReturn(Mono.empty());

        webTestClient.post().uri("/auth/logout")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void userInfo_shouldReturnOk() {
        UserInfoView view = new UserInfoView();
        view.setUsername("testuser");

        when(authUserService.getUserInfo()).thenReturn(Mono.just(view));

        webTestClient.post().uri("/auth/user/info")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void codes_shouldReturnOk() {
        when(authUserService.getUserPermissions()).thenReturn(Mono.just(java.util.List.of("READ", "WRITE")));

        webTestClient.post().uri("/auth/codes")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }
}
