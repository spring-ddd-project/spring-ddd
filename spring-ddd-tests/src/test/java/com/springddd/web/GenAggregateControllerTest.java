package com.springddd.web;

import com.springddd.application.service.gen.GenAggregateCommandService;
import com.springddd.application.service.gen.GenAggregateQueryService;
import com.springddd.application.service.gen.dto.GenAggregateCommand;
import com.springddd.application.service.gen.dto.GenAggregatePageQuery;
import com.springddd.application.service.gen.dto.GenAggregateView;
import com.springddd.domain.util.ApiResponse;
import com.springddd.domain.util.PageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(GenAggregateController.class)
class GenAggregateControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenAggregateQueryService genAggregateQueryService;

    @MockBean
    private GenAggregateCommandService genAggregateCommandService;

    @Test
    @WithMockUser
    void index_shouldReturnOk() {
        GenAggregateView view = new GenAggregateView();
        view.setId(1L);
        view.setAggregateName("Test Aggregate");
        PageResponse<GenAggregateView> pageResponse = new PageResponse<>(List.of(view), 1L, 1, 10);

        when(genAggregateQueryService.index(any(Mono.class))).thenReturn(Mono.just(pageResponse));

        webTestClient.post().uri("/gen/aggregate/index")
                .bodyValue(new GenAggregatePageQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void create_shouldReturnOk() {
        GenAggregateCommand command = new GenAggregateCommand();
        command.setAggregateName("New Aggregate");

        when(genAggregateCommandService.create(any(GenAggregateCommand.class))).thenReturn(Mono.just(1L));

        webTestClient.post().uri("/gen/aggregate/create")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void update_shouldReturnOk() {
        GenAggregateCommand command = new GenAggregateCommand();
        command.setId(1L);
        command.setAggregateName("Updated Aggregate");

        when(genAggregateCommandService.update(any(GenAggregateCommand.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/gen/aggregate/update")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void wipe_shouldReturnOk() {
        when(genAggregateCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete().uri("/gen/aggregate/wipe?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }
}
