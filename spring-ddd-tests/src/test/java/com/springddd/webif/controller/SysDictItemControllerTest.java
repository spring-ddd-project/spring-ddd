package com.springddd.webif.controller;

import com.springddd.application.service.dict.SysDictItemCommandService;
import com.springddd.application.service.dict.SysDictItemQueryService;
import com.springddd.application.service.permission.EntityPathResolver;
import com.springddd.application.service.dict.dto.SysDictItemCommand;
import com.springddd.application.service.dict.dto.SysDictItemPageQuery;
import com.springddd.web.SysDictItemController;
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

@WebFluxTest(controllers = SysDictItemController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration.class
})
@AutoConfigureWebTestClient
class SysDictItemControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysDictItemCommandService commandService;

    @MockBean
    private SysDictItemQueryService queryService;

    @MockBean
    private EntityPathResolver entityPathResolver;

    @Test
    void index_shouldReturnOk() {
        when(queryService.index(any(SysDictItemPageQuery.class))).thenReturn(Mono.empty());
        SysDictItemPageQuery query = new SysDictItemPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        webTestClient.post().uri("/sys/dict/item/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(query)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void recycle_shouldReturnOk() {
        when(queryService.recycle(any(SysDictItemPageQuery.class))).thenReturn(Mono.empty());
        SysDictItemPageQuery query = new SysDictItemPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        webTestClient.post().uri("/sys/dict/item/recycle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(query)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void create_shouldReturnOk() {
        when(commandService.create(any(SysDictItemCommand.class))).thenReturn(Mono.just(1L));
        webTestClient.post().uri("/sys/dict/item/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SysDictItemCommand())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0).jsonPath("$.data").isEqualTo(1);
    }

    @Test
    void update_shouldReturnOk() {
        when(commandService.update(any(SysDictItemCommand.class))).thenReturn(Mono.empty());
        webTestClient.put().uri("/sys/dict/item/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SysDictItemCommand())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void delete_shouldReturnOk() {
        when(commandService.delete(anyList())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/sys/dict/item/delete").queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void restore_shouldReturnOk() {
        when(commandService.restore(anyList())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/sys/dict/item/restore").queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void wipe_shouldReturnOk() {
        when(commandService.wipe(anyList())).thenReturn(Mono.empty());
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/sys/dict/item/wipe").queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }
}
