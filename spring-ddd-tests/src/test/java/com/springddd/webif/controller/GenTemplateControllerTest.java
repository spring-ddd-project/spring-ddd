package com.springddd.webif.controller;

import com.springddd.application.service.gen.GenTemplateCommandService;
import com.springddd.application.service.gen.GenTemplateQueryService;
import com.springddd.application.service.gen.dto.GenTemplateCommand;
import com.springddd.application.service.gen.dto.GenTemplatePageQuery;
import com.springddd.web.GenTemplateController;
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

@WebFluxTest(controllers = GenTemplateController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration.class
})
@AutoConfigureWebTestClient
class GenTemplateControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenTemplateQueryService queryService;

    @MockBean
    private GenTemplateCommandService commandService;


    @Test
    void index_shouldReturnOk() {
        when(queryService.index(any(GenTemplatePageQuery.class))).thenReturn(Mono.empty());
        GenTemplatePageQuery query = new GenTemplatePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        webTestClient.post().uri("/gen/template/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(query)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void recycle_shouldReturnOk() {
        when(queryService.recycle(any(GenTemplatePageQuery.class))).thenReturn(Mono.empty());
        GenTemplatePageQuery query = new GenTemplatePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        webTestClient.post().uri("/gen/template/recycle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(query)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void create_shouldReturnOk() {
        when(commandService.create(any(GenTemplateCommand.class))).thenReturn(Mono.just(1L));
        webTestClient.post().uri("/gen/template/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new GenTemplateCommand())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0).jsonPath("$.data").isEqualTo(1);
    }

    @Test
    void update_shouldReturnOk() {
        when(commandService.update(any(GenTemplateCommand.class))).thenReturn(Mono.empty());
        webTestClient.put().uri("/gen/template/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new GenTemplateCommand())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void delete_shouldReturnOk() {
        when(commandService.delete(anyList())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/gen/template/delete").queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void restore_shouldReturnOk() {
        when(commandService.restore(anyList())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/gen/template/restore").queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void wipe_shouldReturnOk() {
        when(commandService.wipe(anyList())).thenReturn(Mono.empty());
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/gen/template/wipe").queryParam("ids", List.of(1L)).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }
}
