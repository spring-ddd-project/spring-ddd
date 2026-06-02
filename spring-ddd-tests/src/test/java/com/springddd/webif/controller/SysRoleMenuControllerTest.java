package com.springddd.webif.controller;

import com.springddd.application.service.role.SysRoleMenuCommandService;
import com.springddd.application.service.role.SysRoleMenuQueryService;
import com.springddd.web.SysRoleMenuController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = SysRoleMenuController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration.class
})
@AutoConfigureWebTestClient
class SysRoleMenuControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysRoleMenuCommandService commandService;

    @MockBean
    private SysRoleMenuQueryService queryService;

    @Test
    void linkRoleAndMenus_shouldReturnOk() {
        when(commandService.create(anyLong(), anyList())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/sys/role/linkRoleAndMenus")
                        .queryParam("roleId", 1L).queryParam("menuIds", List.of(1L, 2L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void queryMenusByRoleId_shouldReturnOk() {
        when(queryService.queryLinkRoleAndMenus(anyLong())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/sys/role/queryMenusByRoleId")
                        .queryParam("roleId", 1L).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void wipeLinkRoleAndMenus_shouldReturnOk() {
        when(commandService.wipe(anyList())).thenReturn(Mono.empty());
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/sys/role/wipeLinkRoleAndMenus")
                        .queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }
}
