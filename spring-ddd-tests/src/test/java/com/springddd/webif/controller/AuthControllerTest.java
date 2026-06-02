package com.springddd.webif.controller;

import com.springddd.application.service.auth.AuthUserService;
import com.springddd.application.service.auth.dto.LoginUserQuery;
import com.springddd.application.service.auth.dto.LoginUserView;
import com.springddd.application.service.auth.dto.UserInfoView;
import com.springddd.domain.util.ApiResponse;
import com.springddd.web.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class AuthControllerTest {

    @Mock
    private AuthUserService authUserService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(authUserService);
    }

    @Test
    void login_shouldReturnToken_whenValidCredentials() {
        LoginUserQuery query = new LoginUserQuery();
        query.setUsername("admin");
        query.setPassword("password");

        LoginUserView loginView = new LoginUserView();
        loginView.setAccessToken("mock-jwt-token");

        when(authUserService.getToken(any(LoginUserQuery.class))).thenReturn(Mono.just(loginView));

        Mono<ApiResponse> result = authController.login(Mono.just(query));

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(0, response.getCode());
                    assertEquals("Success", response.getMessage());
                    assertNotNull(response.getData());
                })
                .verifyComplete();
    }

    @Test
    void login_shouldReturnEmpty_whenQueryIsEmpty() {
        when(authUserService.getToken(any(LoginUserQuery.class))).thenReturn(Mono.empty());

        Mono<ApiResponse> result = authController.login(Mono.empty());

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(0, response.getCode());
                    assertEquals("Success", response.getMessage());
                    assertNull(response.getData());
                })
                .verifyComplete();
    }

    @Test
    void logout_shouldReturnSuccess_whenClearsCache() {
        when(authUserService.clearCache()).thenReturn(Mono.empty());

        Mono<ApiResponse> result = authController.logout();

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(0, response.getCode());
                    assertEquals("Success", response.getMessage());
                    assertNull(response.getData());
                })
                .verifyComplete();
    }

    @Test
    void userInfo_shouldReturnUserInfo_whenCalled() {
        UserInfoView userInfoView = new UserInfoView();
        userInfoView.setRealName("Admin User");
        userInfoView.setRoles(Arrays.asList("ROLE_ADMIN", "ROLE_USER"));

        when(authUserService.getUserInfo()).thenReturn(Mono.just(userInfoView));

        Mono<ApiResponse> result = authController.userInfo();

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(0, response.getCode());
                    assertEquals("Success", response.getMessage());
                    assertNotNull(response.getData());
                    assertTrue(response.getData() instanceof UserInfoView);
                    UserInfoView actualUserInfo = (UserInfoView) response.getData();
                    assertEquals("Admin User", actualUserInfo.getRealName());
                    assertTrue(actualUserInfo.getRoles().contains("ROLE_ADMIN"));
                })
                .verifyComplete();
    }

    @Test
    void codes_shouldReturnPermissions_whenCalled() {
        List<String> permissions = Arrays.asList("system:user:read", "system:user:write", "system:user:delete");

        when(authUserService.getUserPermissions()).thenReturn(Mono.just(permissions));

        Mono<ApiResponse> result = authController.codes();

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(0, response.getCode());
                    assertEquals("Success", response.getMessage());
                    assertNotNull(response.getData());
                    assertTrue(response.getData() instanceof List);
                    @SuppressWarnings("unchecked")
                    List<String> actualPermissions = (List<String>) response.getData();
                    assertEquals(3, actualPermissions.size());
                    assertTrue(actualPermissions.contains("system:user:read"));
                })
                .verifyComplete();
    }

    @Test
    void codes_shouldReturnEmptyList_whenNoPermissions() {
        when(authUserService.getUserPermissions()).thenReturn(Mono.just(List.of()));

        Mono<ApiResponse> result = authController.codes();

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(0, response.getCode());
                    assertEquals("Success", response.getMessage());
                    assertNotNull(response.getData());
                })
                .verifyComplete();
    }
}