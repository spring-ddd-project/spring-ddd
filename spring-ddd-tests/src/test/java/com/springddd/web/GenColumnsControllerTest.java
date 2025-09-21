package com.springddd.web;

import com.springddd.application.service.gen.GenColumnsCommandService;
import com.springddd.application.service.gen.GenColumnsQueryService;
import com.springddd.application.service.gen.dto.GenColumnsView;
import com.springddd.domain.util.PageResponse;
import com.springddd.web.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(GenColumnsController.class)
@Import(TestSecurityConfig.class)
class GenColumnsControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenColumnsQueryService genColumnsQueryService;

    @MockBean
    private GenColumnsCommandService genColumnsCommandService;

    @Test
    void testGetJavaEntityInfoByInfoId() {
        when(genColumnsQueryService.queryJavaEntityInfoByInfoId(anyLong()))
                .thenReturn(Mono.just(Collections.emptyList()));

        webTestClient.post()
                .uri("/gen/columns/queryJavaEntityInfoByInfoId?infoId=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testGetByInfoId() {
        PageResponse<GenColumnsView> pageResponse = new PageResponse<>(
                Collections.emptyList(), 0, 0, 0
        );
        when(genColumnsQueryService.queryColumnsByGenInfoId(anyLong(), anyString()))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.post()
                .uri("/gen/columns/queryByInfoId?infoId=1&databaseName=testDb")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testCreate() {
        when(genColumnsCommandService.create(any()))
                .thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/gen/columns/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"propColumnName\":\"test_column\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testBatchCreate() {
        when(genColumnsCommandService.batchSave(any()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/gen/columns/batchCreate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("[{}]")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testBatchUpdate() {
        when(genColumnsCommandService.batchUpdate(any()))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/gen/columns/batchUpdate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("[{}]")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testUpdate() {
        when(genColumnsCommandService.update(any()))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/gen/columns/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testDelete() {
        when(genColumnsCommandService.delete(any()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/gen/columns/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testWipe() {
        when(genColumnsCommandService.wipe(any()))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/gen/columns/wipe?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
