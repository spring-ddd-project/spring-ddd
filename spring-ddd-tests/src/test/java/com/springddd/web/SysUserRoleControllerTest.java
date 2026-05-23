package com.springddd.web;

import com.springddd.application.service.user.SysUserRoleCommandService;
import com.springddd.application.service.user.SysUserRoleQueryService;
import com.springddd.application.service.user.dto.SysUserRoleView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

class SysUserRoleControllerTest {

    private WebTestClient webTestClient;
    private SysUserRoleCommandService commandService;
    private SysUserRoleQueryService queryService;

    @BeforeEach
    void setUp() {
        commandService = mock(SysUserRoleCommandService.class);
        queryService = mock(SysUserRoleQueryService.class);
        SysUserRoleController controller = new SysUserRoleController(commandService, queryService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @DisplayName("POST /sys/user/queryRolesByUserId 应返回用户角色列表")
    void queryRolesByUserId_shouldReturnRoles() {
        SysUserRoleView view = new SysUserRoleView();
        view.setUserId(1L);
        view.setRoleId(2L);
        when(queryService.queryLinkUserAndRole(1L)).thenReturn(Mono.just(List.of(view)));

        webTestClient.post()
                .uri("/sys/user/queryRolesByUserId?userId=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data[0].roleId").isEqualTo(2);
    }

    @Test
    @DisplayName("POST /sys/user/linkUserAndRole 应关联成功")
    void linkUserAndRole_shouldSuccess() {
        when(commandService.create(1L, List.of(2L, 3L))).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/user/linkUserAndRole?userId=1&roleIds=2&roleIds=3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("DELETE /sys/user/wipeLinkUserAndRole 应解除关联成功")
    void wipeLinkUserAndRole_shouldSuccess() {
        when(commandService.wipe(anyList())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/sys/user/wipeLinkUserAndRole")
                        .queryParam("ids", 1, 2)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
