package com.springddd.web;

import com.springddd.application.service.gen.GenColumnsCommandService;
import com.springddd.application.service.gen.GenColumnsQueryService;
import com.springddd.application.service.gen.dto.GenColumnsView;
import com.springddd.domain.util.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

class GenColumnsControllerTest {

    private WebTestClient webTestClient;
    private GenColumnsQueryService queryService;
    private GenColumnsCommandService commandService;

    @BeforeEach
    void setUp() {
        queryService = mock(GenColumnsQueryService.class);
        commandService = mock(GenColumnsCommandService.class);
        GenColumnsController controller = new GenColumnsController(queryService, commandService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @DisplayName("POST /gen/columns/queryJavaEntityInfoByInfoId 应返回Java实体信息")
    void queryJavaEntityInfoByInfoId_shouldReturnInfo() {
        GenColumnsView view = new GenColumnsView();
        view.setPropJavaEntity("SysUser");
        when(queryService.queryJavaEntityInfoByInfoId(1L)).thenReturn(Mono.just(List.of(view)));

        webTestClient.post()
                .uri("/gen/columns/queryJavaEntityInfoByInfoId?infoId=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data[0].propJavaEntity").isEqualTo("SysUser");
    }

    @Test
    @DisplayName("POST /gen/columns/queryByInfoId 应返回列信息分页")
    void queryByInfoId_shouldReturnColumns() {
        GenColumnsView view = new GenColumnsView();
        view.setPropColumnName("username");
        PageResponse<GenColumnsView> page = new PageResponse<>(
                List.of(view), 1L, 1, 10
        );
        when(queryService.queryColumnsByGenInfoId(1L, "spring_ddd")).thenReturn(Mono.just(page));

        webTestClient.post()
                .uri("/gen/columns/queryByInfoId?infoId=1&databaseName=spring_ddd")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data.items[0].propColumnName").isEqualTo("username");
    }

    @Test
    @DisplayName("POST /gen/columns/create 应创建成功")
    void create_shouldSuccess() {
        when(commandService.create(any())).thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/gen/columns/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"propColumnName\":\"username\",\"propColumnType\":\"varchar\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data").isEqualTo(1);
    }

    @Test
    @DisplayName("POST /gen/columns/batchCreate 应批量创建成功")
    void batchCreate_shouldSuccess() {
        when(commandService.batchSave(anyList())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/gen/columns/batchCreate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("[{\"propColumnName\":\"username\"}]")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("PUT /gen/columns/batchUpdate 应批量更新成功")
    void batchUpdate_shouldSuccess() {
        when(commandService.batchUpdate(anyList())).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/gen/columns/batchUpdate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("[{\"id\":1,\"propColumnName\":\"updated\"}]")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("PUT /gen/columns/update 应更新成功")
    void update_shouldSuccess() {
        when(commandService.update(any())).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/gen/columns/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1,\"propColumnName\":\"updated\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /gen/columns/delete 应删除成功")
    void delete_shouldSuccess() {
        when(commandService.delete(any())).thenReturn(Mono.empty());

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
    @DisplayName("DELETE /gen/columns/wipe 应彻底删除成功")
    void wipe_shouldSuccess() {
        when(commandService.wipe(anyList())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/gen/columns/wipe")
                        .queryParam("ids", 1, 2)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
