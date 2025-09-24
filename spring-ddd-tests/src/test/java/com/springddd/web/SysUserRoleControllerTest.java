package com.springddd.web;

import com.springddd.application.service.user.SysUserRoleCommandService;
import com.springddd.application.service.user.SysUserRoleQueryService;
import com.springddd.domain.util.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(SysUserRoleController.class)
class SysUserRoleControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysUserRoleQueryService sysUserRoleQueryService;

    @MockBean
    private SysUserRoleCommandService sysUserRoleCommandService;

    @Test
    @WithMockUser
    void queryRolesByUserId_shouldReturnOk() {
        when(sysUserRoleQueryService.queryLinkUserAndRole(any())).thenReturn(Mono.just(List.of(1L, 2L)));

        webTestClient.post().uri("/sys/user/queryRolesByUserId?userId=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void linkUserAndRole_shouldReturnOk() {
        when(sysUserRoleCommandService.create(any(), any())).thenReturn(Mono.empty());

        webTestClient.post().uri("/sys/user/linkUserAndRole?userId=1&roleIds=1&roleIds=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void wipeLinkUserAndRole_shouldReturnOk() {
        when(sysUserRoleCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete().uri("/sys/user/wipeLinkUserAndRole?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }
}
