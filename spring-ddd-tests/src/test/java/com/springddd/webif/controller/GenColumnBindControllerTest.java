package com.springddd.webif.controller;

import com.springddd.application.service.gen.GenColumnBindCommandService;
import com.springddd.application.service.gen.GenColumnBindQueryService;
import com.springddd.application.service.gen.dto.GenColumnBindCommand;
import com.springddd.application.service.gen.dto.GenColumnBindPageQuery;
import com.springddd.web.GenColumnBindController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = GenColumnBindController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration.class
})
@AutoConfigureWebTestClient
class GenColumnBindControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenColumnBindCommandService commandService;

    @MockBean
    private GenColumnBindQueryService queryService;

    @Test
    void index_shouldReturnOk() {
        when(queryService.index(any(GenColumnBindPageQuery.class))).thenReturn(Mono.empty());
        GenColumnBindPageQuery query = new GenColumnBindPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        webTestClient.post().uri("/gen/column/bind/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(query)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void recycle_shouldReturnOk() {
        when(queryService.recycle(any(GenColumnBindPageQuery.class))).thenReturn(Mono.empty());
        GenColumnBindPageQuery query = new GenColumnBindPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        webTestClient.post().uri("/gen/column/bind/recycle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(query)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void create_shouldReturnOk() {
        when(commandService.create(any(GenColumnBindCommand.class))).thenReturn(Mono.just(1L));
        webTestClient.post().uri("/gen/column/bind/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new GenColumnBindCommand())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0).jsonPath("$.data").isEqualTo(1);
    }

    @Test
    void update_shouldReturnOk() {
        when(commandService.update(any(GenColumnBindCommand.class))).thenReturn(Mono.empty());
        webTestClient.put().uri("/gen/column/bind/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new GenColumnBindCommand())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void delete_shouldReturnOk() {
        when(commandService.delete(anyList())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/gen/column/bind/delete").queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void wipe_shouldReturnOk() {
        when(commandService.wipe(anyList())).thenReturn(Mono.empty());
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/gen/column/bind/wipe").queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void restore_shouldReturnOk() {
        when(commandService.restore(anyList())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/gen/column/bind/restore").queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }
}
