package com.springddd.web;

import com.springddd.application.service.gen.GenTemplateCommandService;
import com.springddd.application.service.gen.GenTemplateQueryService;
import com.springddd.application.service.gen.dto.GenTemplateView;
import com.springddd.domain.util.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

class GenTemplateControllerTest {

    private WebTestClient webTestClient;
    private GenTemplateQueryService queryService;
    private GenTemplateCommandService commandService;

    @BeforeEach
    void setUp() {
        queryService = mock(GenTemplateQueryService.class);
        commandService = mock(GenTemplateCommandService.class);
        GenTemplateController controller = new GenTemplateController(queryService, commandService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @DisplayName("POST /gen/template/index 应返回分页列表")
    void index_shouldReturnPage() {
        GenTemplateView view = new GenTemplateView();
        view.setId(1L);
        view.setTemplateName("test");
        PageResponse<GenTemplateView> page = new PageResponse<>(
                List.of(view), 1L, 1, 10
        );
        when(queryService.index(any())).thenReturn(Mono.just(page));

        webTestClient.post()
                .uri("/gen/template/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data.list[0].templateName").isEqualTo("test");
    }

    @Test
    @DisplayName("POST /gen/template/recycle 应返回回收站分页")
    void recycle_shouldReturnRecyclePage() {
        GenTemplateView view = new GenTemplateView();
        view.setTemplateName("deleted");
        PageResponse<GenTemplateView> page = new PageResponse<>(
                List.of(view), 1L, 1, 10
        );
        when(queryService.recycle(any())).thenReturn(Mono.just(page));

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
    @DisplayName("POST /gen/template/create 应创建成功")
    void create_shouldSuccess() {
        when(commandService.create(any())).thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/gen/template/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"templateName\":\"new\",\"templateContent\":\"content\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data").isEqualTo(1);
    }

    @Test
    @DisplayName("PUT /gen/template/update 应更新成功")
    void update_shouldSuccess() {
        when(commandService.update(any())).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/gen/template/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1,\"templateName\":\"updated\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /gen/template/delete 应删除成功")
    void delete_shouldSuccess() {
        when(commandService.delete(anyList())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/gen/template/delete?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /gen/template/restore 应恢复成功")
    void restore_shouldSuccess() {
        when(commandService.restore(anyList())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/gen/template/restore?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("DELETE /gen/template/wipe 应彻底删除成功")
    void wipe_shouldSuccess() {
        when(commandService.wipe(anyList())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/gen/template/wipe")
                        .queryParam("ids", 1, 2)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
