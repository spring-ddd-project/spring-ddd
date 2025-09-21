package com.springddd.web;

import com.springddd.application.service.gen.GenTemplateCommandService;
import com.springddd.application.service.gen.GenTemplateQueryService;
import com.springddd.application.service.gen.dto.GenTemplatePageQuery;
import com.springddd.application.service.gen.dto.GenTemplateView;
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

@WebFluxTest(GenTemplateController.class)
@Import(TestSecurityConfig.class)
class GenTemplateControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenTemplateQueryService genTemplateQueryService;

    @MockBean
    private GenTemplateCommandService genTemplateCommandService;

    @Test
    void testIndex() {
        PageResponse<GenTemplateView> pageResponse = new PageResponse<>(
                Collections.emptyList(), 0, 1, 10
        );
        when(genTemplateQueryService.index(any(GenTemplatePageQuery.class)))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.post()
                .uri("/gen/template/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testRecycle() {
        PageResponse<GenTemplateView> pageResponse = new PageResponse<>(
                Collections.emptyList(), 0, 1, 10
        );
        when(genTemplateQueryService.recycle(any(GenTemplatePageQuery.class)))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.post()
                .uri("/gen/template/recycle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testCreate() {
        when(genTemplateCommandService.create(any()))
                .thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/gen/template/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"templateName\":\"Test Template\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testUpdate() {
        when(genTemplateCommandService.update(any()))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/gen/template/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testDelete() {
        when(genTemplateCommandService.delete(any()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/gen/template/delete?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testRestore() {
        when(genTemplateCommandService.restore(any()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/gen/template/restore?ids=1,2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testWipe() {
        when(genTemplateCommandService.wipe(any()))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/gen/template/wipe?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
