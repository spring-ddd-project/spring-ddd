package com.springddd.webif.controller;

import com.springddd.application.service.gen.GenTableInfoCommandService;
import com.springddd.application.service.gen.GenTableInfoQueryService;
import com.springddd.application.service.permission.EntityPathResolver;
import com.springddd.application.service.gen.dto.GenTableInfoPageQuery;
import com.springddd.web.GenTableController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = GenTableController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration.class
})
@AutoConfigureWebTestClient
class GenTableControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenTableInfoQueryService queryService;

    @MockBean
    private GenTableInfoCommandService commandService;

    @MockBean
    private EntityPathResolver entityPathResolver;

    @Test
    void tableIndex_shouldReturnOk() {
        when(queryService.index(any(GenTableInfoPageQuery.class))).thenReturn(Mono.empty());
        GenTableInfoPageQuery query = new GenTableInfoPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        webTestClient.post().uri("/gen/table/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(query)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void preview_shouldReturnOk() {
        when(queryService.preview()).thenReturn(Mono.empty());
        webTestClient.post().uri("/gen/table/preview")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void download_shouldReturnOk() {
        Resource resource = new ByteArrayResource("test".getBytes());
        ResponseEntity<Resource> responseEntity = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=test.zip")
                .body(resource);
        when(commandService.download()).thenReturn(Mono.just(responseEntity));
        webTestClient.get().uri("/gen/table/download")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void wipe_shouldReturnOk() {
        when(commandService.wipe()).thenReturn(Mono.empty());
        webTestClient.delete().uri("/gen/table/wipe")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void generate_shouldReturnOk() {
        when(commandService.generate(anyString())).thenReturn(Mono.empty());
        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/gen/table/generate").queryParam("tableName", "test").build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.code").isEqualTo(0);
    }
}
