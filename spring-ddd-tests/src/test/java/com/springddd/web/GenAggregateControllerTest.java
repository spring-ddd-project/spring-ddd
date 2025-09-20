package com.springddd.web;

import com.springddd.application.service.gen.GenAggregateCommandService;
import com.springddd.application.service.gen.GenAggregateQueryService;
import com.springddd.application.service.gen.dto.GenAggregatePageQuery;
import com.springddd.application.service.gen.dto.GenAggregateView;
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

@WebFluxTest(GenAggregateController.class)
@Import(TestSecurityConfig.class)
class GenAggregateControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenAggregateQueryService genAggregateQueryService;

    @MockBean
    private GenAggregateCommandService genAggregateCommandService;

    @Test
    void testIndex() {
        PageResponse<GenAggregateView> pageResponse = new PageResponse<>(
                Collections.emptyList(), 0, 1, 10
        );
        when(genAggregateQueryService.index(any(GenAggregatePageQuery.class)))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.post()
                .uri("/gen/aggregate/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"infoId\":1}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testCreate() {
        when(genAggregateCommandService.create(any()))
                .thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/gen/aggregate/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"objectName\":\"test\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testUpdate() {
        when(genAggregateCommandService.update(any()))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/gen/aggregate/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testWipe() {
        when(genAggregateCommandService.wipe(any()))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/gen/aggregate/wipe?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
