package com.springddd.web;

import com.springddd.application.service.gen.GenAggregateCommandService;
import com.springddd.application.service.gen.GenAggregateQueryService;
import com.springddd.application.service.gen.dto.GenAggregateCommand;
import com.springddd.application.service.gen.dto.GenAggregatePageQuery;
import com.springddd.application.service.gen.dto.GenAggregateView;
import com.springddd.domain.util.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

class GenAggregateControllerTest {

    private WebTestClient webTestClient;
    private GenAggregateCommandService commandService;
    private GenAggregateQueryService queryService;

    @BeforeEach
    void setUp() {
        commandService = mock(GenAggregateCommandService.class);
        queryService = mock(GenAggregateQueryService.class);
        GenAggregateController controller = new GenAggregateController(commandService, queryService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @DisplayName("POST /gen/aggregate/index 应返回分页列表")
    void index_shouldReturnPage() {
        GenAggregateView view = new GenAggregateView();
        view.setId(1L);
        view.setObjectName("test");
        PageResponse<GenAggregateView> page = new PageResponse<>(
                List.of(view), 1L, 1, 10
        );
        when(queryService.index(any())).thenReturn(Mono.just(page));

        webTestClient.post()
                .uri("/gen/aggregate/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data.items[0].objectName").isEqualTo("test");
    }

    @Test
    @DisplayName("POST /gen/aggregate/create 应创建成功")
    void create_shouldSuccess() {
        when(commandService.create(any())).thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/gen/aggregate/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"objectName\":\"test\",\"objectValue\":\"value\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data").isEqualTo(1);
    }

    @Test
    @DisplayName("PUT /gen/aggregate/update 应更新成功")
    void update_shouldSuccess() {
        when(commandService.update(any())).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/gen/aggregate/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1,\"objectName\":\"updated\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("DELETE /gen/aggregate/wipe 应删除成功")
    void wipe_shouldSuccess() {
        when(commandService.wipe(anyList())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/gen/aggregate/wipe")
                        .queryParam("ids", 1, 2)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /gen/aggregate/create 异常时应返回错误")
    void create_shouldReturnError_whenException() {
        when(commandService.create(any())).thenReturn(Mono.error(new RuntimeException("create failed")));

        webTestClient.post()
                .uri("/gen/aggregate/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"objectName\":\"test\"}")
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
