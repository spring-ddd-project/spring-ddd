package com.springddd.webif.controller;

import com.springddd.application.service.menu.SysMenuCommandService;
import com.springddd.application.service.menu.SysMenuQueryService;
import com.springddd.application.service.permission.EntityPathResolver;
import com.springddd.application.service.menu.dto.SysMenuCommand;
import com.springddd.application.service.menu.dto.SysMenuQuery;
import com.springddd.web.SysMenuController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = SysMenuController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration.class
})
@AutoConfigureWebTestClient
class SysMenuControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysMenuQueryService queryService;

    @MockBean
    private SysMenuCommandService commandService;

    @MockBean
    private EntityPathResolver entityPathResolver;

    @Test
    void index_shouldReturnOk() {
        when(queryService.index(any(SysMenuQuery.class))).thenReturn(Mono.empty());
        webTestClient.post().uri("/sys/menu/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SysMenuQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void recycle_shouldReturnOk() {
        when(queryService.recycle(any(SysMenuQuery.class))).thenReturn(Mono.empty());
        webTestClient.post().uri("/sys/menu/recycle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SysMenuQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void all_shouldReturnOk() {
        when(queryService.queryByPermissions()).thenReturn(Mono.empty());
        webTestClient.post().uri("/sys/menu/all")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void getMenuTreeWithoutPermission_shouldReturnOk() {
        when(queryService.getMenuTreeWithoutPermission()).thenReturn(Mono.empty());
        webTestClient.post().uri("/sys/menu/getMenuTreeWithoutPermission")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void getMenuTreeWithPermission_shouldReturnOk() {
        when(queryService.getMenuTreeWithPermission()).thenReturn(Mono.empty());
        webTestClient.post().uri("/sys/menu/getMenuTreeWithPermission")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void getByMenuId_shouldReturnOk() {
        when(queryService.queryByMenuId(anyLong())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/sys/menu/getByMenuId").queryParam("menuId", 1L).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void create_shouldReturnOk() {
        when(commandService.create(any(SysMenuCommand.class))).thenReturn(Mono.just(1L));
        webTestClient.post().uri("/sys/menu/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SysMenuCommand())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0).jsonPath("$.data").isEqualTo(1);
    }

    @Test
    void update_shouldReturnOk() {
        when(commandService.update(any(SysMenuCommand.class))).thenReturn(Mono.empty());
        webTestClient.put().uri("/sys/menu/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SysMenuCommand())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void delete_shouldReturnOk() {
        when(commandService.delete(anyList())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/sys/menu/delete").queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void restore_shouldReturnOk() {
        when(commandService.restore(anyList())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/sys/menu/restore").queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void wipe_shouldReturnOk() {
        when(commandService.wipe(anyList())).thenReturn(Mono.empty());
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/sys/menu/wipe").queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }
}
