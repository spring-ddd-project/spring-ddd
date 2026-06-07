package com.springddd.webif.controller;

import com.springddd.application.service.user.SysUserRoleCommandService;
import com.springddd.application.service.user.SysUserRoleQueryService;
import com.springddd.web.SysUserRoleController;
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

@WebFluxTest(controllers = SysUserRoleController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration.class
})
@AutoConfigureWebTestClient
class SysUserRoleControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysUserRoleCommandService commandService;

    @MockBean
    private SysUserRoleQueryService queryService;


    @Test
    void queryRolesByUserId_shouldReturnOk() {
        when(queryService.queryLinkUserAndRole(anyLong())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/sys/user/queryRolesByUserId")
                        .queryParam("userId", 1L).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void linkUserAndRole_shouldReturnOk() {
        when(commandService.create(anyLong(), anyList())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/sys/user/linkUserAndRole")
                        .queryParam("userId", 1L).queryParam("roleIds", List.of(1L, 2L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void wipeLinkUserAndRole_shouldReturnOk() {
        when(commandService.wipe(anyList())).thenReturn(Mono.empty());
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/sys/user/wipeLinkUserAndRole")
                        .queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }
}
