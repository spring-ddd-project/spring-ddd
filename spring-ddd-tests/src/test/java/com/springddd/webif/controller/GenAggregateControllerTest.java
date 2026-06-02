package com.springddd.webif.controller;

import com.springddd.application.service.gen.GenAggregateCommandService;
import com.springddd.application.service.gen.GenAggregateQueryService;
import com.springddd.application.service.gen.dto.GenAggregateCommand;
import com.springddd.application.service.gen.dto.GenAggregatePageQuery;
import com.springddd.web.GenAggregateController;
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

@WebFluxTest(controllers = GenAggregateController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration.class
})
@AutoConfigureWebTestClient
class GenAggregateControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenAggregateCommandService commandService;

    @MockBean
    private GenAggregateQueryService queryService;

    @Test
    void index_shouldReturnOk() {
        when(queryService.index(any(GenAggregatePageQuery.class))).thenReturn(Mono.empty());

        GenAggregatePageQuery query = new GenAggregatePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        webTestClient.post()
                .uri("/gen/aggregate/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(query)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.message").isEqualTo("Success");
    }

    @Test
    void create_shouldReturnOk() {
        when(commandService.create(any(GenAggregateCommand.class))).thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/gen/aggregate/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new GenAggregateCommand())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data").isEqualTo(1);
    }

    @Test
    void update_shouldReturnOk() {
        when(commandService.update(any(GenAggregateCommand.class))).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/gen/aggregate/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new GenAggregateCommand())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void wipe_shouldReturnOk() {
        when(commandService.wipe(anyList())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/gen/aggregate/wipe")
                        .queryParam("ids", List.of(1L, 2L))
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
