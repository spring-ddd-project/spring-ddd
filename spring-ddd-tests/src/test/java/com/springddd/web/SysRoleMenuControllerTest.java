package com.springddd.web;

import com.springddd.application.service.role.SysRoleMenuCommandService;
import com.springddd.application.service.role.SysRoleMenuQueryService;
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

@WebFluxTest(SysRoleMenuController.class)
class SysRoleMenuControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysRoleMenuQueryService sysRoleMenuQueryService;

    @MockBean
    private SysRoleMenuCommandService sysRoleMenuCommandService;

    @Test
    @WithMockUser
    void linkRoleAndMenus_shouldReturnOk() {
        when(sysRoleMenuCommandService.create(any(), any())).thenReturn(Mono.empty());

        webTestClient.post().uri("/sys/role/linkRoleAndMenus?roleId=1&menuIds=1&menuIds=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void queryMenusByRoleId_shouldReturnOk() {
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(any())).thenReturn(Mono.just(List.of(1L, 2L)));

        webTestClient.post().uri("/sys/role/queryMenusByRoleId?roleId=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void wipeLinkRoleAndMenus_shouldReturnOk() {
        when(sysRoleMenuCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete().uri("/sys/role/wipeLinkRoleAndMenus?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }
}
