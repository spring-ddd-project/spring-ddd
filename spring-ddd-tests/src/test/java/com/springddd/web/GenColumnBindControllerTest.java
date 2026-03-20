package com.springddd.web;

import com.springddd.application.service.gen.GenColumnBindCommandService;
import com.springddd.application.service.gen.GenColumnBindQueryService;
import com.springddd.application.service.gen.dto.GenColumnBindPageQuery;
import com.springddd.application.service.gen.dto.GenColumnBindView;
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
import static org.mockito.Mockito.when;

@WebFluxTest(GenColumnBindController.class)
@Import(TestSecurityConfig.class)
class GenColumnBindControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenColumnBindQueryService genColumnBindQueryService;

    @MockBean
    private GenColumnBindCommandService genColumnBindCommandService;

    @Test
    void testIndex() {
        PageResponse<GenColumnBindView> pageResponse = new PageResponse<>(
                Collections.emptyList(), 0, 1, 10
        );
        when(genColumnBindQueryService.index(any(GenColumnBindPageQuery.class)))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.post()
                .uri("/gen/column/bind/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testRecycle() {
        PageResponse<GenColumnBindView> pageResponse = new PageResponse<>(
                Collections.emptyList(), 0, 1, 10
        );
        when(genColumnBindQueryService.recycle(any(GenColumnBindPageQuery.class)))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.post()
                .uri("/gen/column/bind/recycle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testCreate() {
        when(genColumnBindCommandService.create(any()))
                .thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/gen/column/bind/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"columnType\":\"varchar\",\"entityType\":\"String\",\"componentType\":1}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testUpdate() {
        when(genColumnBindCommandService.update(any()))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/gen/column/bind/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testDelete() {
        when(genColumnBindCommandService.delete(any()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/gen/column/bind/delete?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testWipe() {
        when(genColumnBindCommandService.wipe(any()))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/gen/column/bind/wipe?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testRestore() {
        when(genColumnBindCommandService.restore(any()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/gen/column/bind/restore?ids=1,2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
