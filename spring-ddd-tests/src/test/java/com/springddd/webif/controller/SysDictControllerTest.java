package com.springddd.webif.controller;

import com.springddd.application.service.dict.SysDictCommandService;
import com.springddd.application.service.dict.SysDictQueryService;
import com.springddd.application.service.dict.dto.SysDictCommand;
import com.springddd.application.service.dict.dto.SysDictPageQuery;
import com.springddd.web.SysDictController;
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

@WebFluxTest(controllers = SysDictController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration.class
})
@AutoConfigureWebTestClient
class SysDictControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysDictCommandService commandService;

    @MockBean
    private SysDictQueryService queryService;

    @Test
    void index_shouldReturnOk() {
        when(queryService.index(any(SysDictPageQuery.class))).thenReturn(Mono.empty());
        SysDictPageQuery query = new SysDictPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        webTestClient.post().uri("/sys/dict/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(query)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void queryAll_shouldReturnOk() {
        when(queryService.queryAll()).thenReturn(Mono.empty());
        webTestClient.post().uri("/sys/dict/queryAll")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void queryItemByDictCode_shouldReturnOk() {
        when(queryService.queryDictByCode(anyString())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/sys/dict/queryItemByDictCode").queryParam("dictCode", "status").build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void recycle_shouldReturnOk() {
        when(queryService.recycle(any(SysDictPageQuery.class))).thenReturn(Mono.empty());
        SysDictPageQuery query = new SysDictPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        webTestClient.post().uri("/sys/dict/recycle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(query)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void getItemLabel_shouldReturnOk() {
        when(queryService.queryItemLabelByDictCode(anyString(), anyInt())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/sys/dict/getItemLabel")
                        .queryParam("dictCode", "status").queryParam("itemValue", 1).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void create_shouldReturnOk() {
        when(commandService.create(any(SysDictCommand.class))).thenReturn(Mono.just(1L));
        webTestClient.post().uri("/sys/dict/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SysDictCommand())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0).jsonPath("$.data").isEqualTo(1);
    }

    @Test
    void update_shouldReturnOk() {
        when(commandService.update(any(SysDictCommand.class))).thenReturn(Mono.empty());
        webTestClient.put().uri("/sys/dict/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SysDictCommand())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void delete_shouldReturnOk() {
        when(commandService.delete(anyList())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/sys/dict/delete").queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void wipe_shouldReturnOk() {
        when(commandService.wipe(anyList())).thenReturn(Mono.empty());
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/sys/dict/wipe").queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void restore_shouldReturnOk() {
        when(commandService.restore(anyList())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/sys/dict/restore").queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }
}
