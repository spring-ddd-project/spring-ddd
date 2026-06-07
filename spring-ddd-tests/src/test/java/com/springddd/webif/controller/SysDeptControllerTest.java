package com.springddd.webif.controller;

import com.springddd.application.service.dept.SysDeptCommandService;
import com.springddd.application.service.dept.SysDeptQueryService;
import com.springddd.application.service.dept.dto.SysDeptCommand;
import com.springddd.application.service.dept.dto.SysDeptQuery;
import com.springddd.web.SysDeptController;
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

@WebFluxTest(controllers = SysDeptController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration.class
})
@AutoConfigureWebTestClient
class SysDeptControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysDeptQueryService queryService;

    @MockBean
    private SysDeptCommandService commandService;


    @Test
    void index_shouldReturnOk() {
        when(queryService.index(any(SysDeptQuery.class))).thenReturn(Mono.empty());
        webTestClient.post().uri("/sys/dept/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SysDeptQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void recycle_shouldReturnOk() {
        when(queryService.recycle(any(SysDeptQuery.class))).thenReturn(Mono.empty());
        webTestClient.post().uri("/sys/dept/recycle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SysDeptQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void tree_shouldReturnOk() {
        when(queryService.deptTree()).thenReturn(Mono.empty());
        webTestClient.post().uri("/sys/dept/tree")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void create_shouldReturnOk() {
        when(commandService.create(any(SysDeptCommand.class))).thenReturn(Mono.just(1L));
        webTestClient.post().uri("/sys/dept/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SysDeptCommand())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0).jsonPath("$.data").isEqualTo(1);
    }

    @Test
    void update_shouldReturnOk() {
        when(commandService.update(any(SysDeptCommand.class))).thenReturn(Mono.empty());
        webTestClient.put().uri("/sys/dept/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SysDeptCommand())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void delete_shouldReturnOk() {
        when(commandService.delete(anyList())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/sys/dept/delete").queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void restore_shouldReturnOk() {
        when(commandService.restore(anyList())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/sys/dept/restore").queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void wipe_shouldReturnOk() {
        when(commandService.wipe(anyList())).thenReturn(Mono.empty());
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/sys/dept/wipe").queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }
}
