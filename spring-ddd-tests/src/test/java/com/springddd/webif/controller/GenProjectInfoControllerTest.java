package com.springddd.webif.controller;

import com.springddd.application.service.gen.GenProjectInfoCommandService;
import com.springddd.application.service.gen.GenProjectInfoQueryService;
import com.springddd.application.service.gen.dto.GenProjectInfoCommand;
import com.springddd.application.service.gen.dto.GenProjectInfoQuery;
import com.springddd.web.GenProjectInfoController;
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

@WebFluxTest(controllers = GenProjectInfoController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration.class
})
@AutoConfigureWebTestClient
class GenProjectInfoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenProjectInfoCommandService commandService;

    @MockBean
    private GenProjectInfoQueryService queryService;

    @Test
    void index_shouldReturnOk() {
        when(queryService.index(any(GenProjectInfoQuery.class))).thenReturn(Mono.empty());
        webTestClient.post().uri("/gen/projectInfo/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new GenProjectInfoQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void queryInfoByTableName_shouldReturnOk() {
        when(queryService.queryGenInfoByTableName(anyString())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/gen/projectInfo/queryInfoByTableName").queryParam("tableName", "test").build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void create_shouldReturnOk() {
        when(commandService.create(any(GenProjectInfoCommand.class))).thenReturn(Mono.just(1L));
        webTestClient.post().uri("/gen/projectInfo/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new GenProjectInfoCommand())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0).jsonPath("$.data").isEqualTo(1);
    }

    @Test
    void update_shouldReturnOk() {
        when(commandService.update(any(GenProjectInfoCommand.class))).thenReturn(Mono.empty());
        webTestClient.put().uri("/gen/projectInfo/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new GenProjectInfoCommand())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void delete_shouldReturnOk() {
        when(commandService.delete(any(GenProjectInfoCommand.class))).thenReturn(Mono.empty());
        webTestClient.post().uri("/gen/projectInfo/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new GenProjectInfoCommand())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void wipe_shouldReturnOk() {
        when(commandService.wipeByIds(anyList())).thenReturn(Mono.empty());
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/gen/projectInfo/wipe").queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }
}
