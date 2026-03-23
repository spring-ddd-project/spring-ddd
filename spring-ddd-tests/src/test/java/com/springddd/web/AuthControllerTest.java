package com.springddd.web;

import com.springddd.application.service.auth.AuthUserService;
import com.springddd.application.service.auth.dto.LoginUserQuery;
import com.springddd.application.service.auth.dto.LoginUserView;
import com.springddd.application.service.auth.dto.UserInfoView;
import com.springddd.web.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(AuthController.class)
@Import(TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthUserService authUserService;

    @Test
    void testLogin() {
        LoginUserView loginView = new LoginUserView();
        loginView.setAccessToken("jwt-token");
        when(authUserService.getToken(any(LoginUserQuery.class)))
                .thenReturn(Mono.just(loginView));

        webTestClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"username\":\"admin\",\"password\":\"admin123\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testLogout() {
        when(authUserService.clearCache()).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/auth/logout")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testUserInfo() {
        UserInfoView userInfoView = new UserInfoView();
        userInfoView.setRealName("Test User");
        when(authUserService.getUserInfo()).thenReturn(Mono.just(userInfoView));

        webTestClient.post()
                .uri("/auth/user/info")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testCodes() {
        List<String> permissions = Arrays.asList("code1", "code2");
        when(authUserService.getUserPermissions()).thenReturn(Mono.just(permissions));

        webTestClient.post()
                .uri("/auth/codes")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data[0]").isEqualTo("code1")
                .jsonPath("$.data[1]").isEqualTo("code2");
    }
}
