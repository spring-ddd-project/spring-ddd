package com.springddd.web;

import com.springddd.application.service.gen.GenTableInfoCommandService;
import com.springddd.application.service.gen.GenTableInfoQueryService;
import com.springddd.application.service.gen.dto.GenTableInfoPageQuery;
import com.springddd.domain.util.ApiResponse;
import com.springddd.domain.util.PageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(GenTableController.class)
class GenTableControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenTableInfoQueryService genTableInfoQueryService;

    @MockBean
    private GenTableInfoCommandService genTableInfoCommandService;

    @Test
    @WithMockUser
    void tableIndex_shouldReturnOk() {
        PageResponse<Object> pageResponse = new PageResponse<>(java.util.List.of(), 0L, 1, 10);

        when(genTableInfoQueryService.index(any(Mono.class))).thenReturn(Mono.just(pageResponse));

        webTestClient.post().uri("/gen/table/index")
                .bodyValue(new GenTableInfoPageQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void preview_shouldReturnOk() {
        when(genTableInfoQueryService.preview()).thenReturn(Mono.empty());

        webTestClient.post().uri("/gen/table/preview")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void download_shouldReturnOk() {
        when(genTableInfoCommandService.download()).thenReturn(Mono.just(ResponseEntity.ok().<Resource>build()));

        webTestClient.get().uri("/gen/table/download")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockUser
    void wipe_shouldReturnOk() {
        when(genTableInfoCommandService.wipe()).thenReturn(Mono.empty());

        webTestClient.delete().uri("/gen/table/wipe")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void generate_shouldReturnOk() {
        when(genTableInfoCommandService.generate(any())).thenReturn(Mono.empty());

        webTestClient.post().uri("/gen/table/generate?tableName=test_table")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }
}
