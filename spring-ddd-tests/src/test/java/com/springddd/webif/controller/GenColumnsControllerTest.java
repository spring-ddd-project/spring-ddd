package com.springddd.webif.controller;

import com.springddd.application.service.gen.GenColumnsCommandService;
import com.springddd.application.service.gen.GenColumnsQueryService;
import com.springddd.application.service.gen.dto.GenColumnsCommand;
import com.springddd.web.GenColumnsController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = GenColumnsController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration.class
})
@AutoConfigureWebTestClient
class GenColumnsControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenColumnsCommandService commandService;

    @MockBean
    private GenColumnsQueryService queryService;

    @Test
    void queryJavaEntityInfoByInfoId_shouldReturnOk() {
        when(queryService.queryJavaEntityInfoByInfoId(anyLong())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/gen/columns/queryJavaEntityInfoByInfoId").queryParam("infoId", 1L).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void queryByInfoId_shouldReturnOk() {
        when(queryService.queryColumnsByGenInfoId(anyLong(), anyString())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/gen/columns/queryByInfoId")
                        .queryParam("infoId", 1L).queryParam("databaseName", "test").build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void create_shouldReturnOk() {
        when(commandService.create(any(GenColumnsCommand.class))).thenReturn(Mono.just(1L));
        webTestClient.post().uri("/gen/columns/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new GenColumnsCommand())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0).jsonPath("$.data").isEqualTo(1);
    }

    @Test
    void batchCreate_shouldReturnOk() {
        when(commandService.batchSave(anyList())).thenReturn(Mono.empty());
        webTestClient.post().uri("/gen/columns/batchCreate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Collections.singletonList(new GenColumnsCommand()))
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void batchUpdate_shouldReturnOk() {
        when(commandService.batchUpdate(anyList())).thenReturn(Mono.empty());
        webTestClient.put().uri("/gen/columns/batchUpdate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Collections.singletonList(new GenColumnsCommand()))
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void update_shouldReturnOk() {
        when(commandService.update(any(GenColumnsCommand.class))).thenReturn(Mono.empty());
        webTestClient.put().uri("/gen/columns/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new GenColumnsCommand())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void delete_shouldReturnOk() {
        when(commandService.delete(any(GenColumnsCommand.class))).thenReturn(Mono.empty());
        webTestClient.post().uri("/gen/columns/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new GenColumnsCommand())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void wipe_shouldReturnOk() {
        when(commandService.wipe(anyList())).thenReturn(Mono.empty());
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/gen/columns/wipe").queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }
}
