package com.springddd.web;

import com.springddd.application.service.gen.GenProjectInfoCommandService;
import com.springddd.application.service.gen.GenProjectInfoQueryService;
import com.springddd.application.service.gen.dto.GenProjectInfoView;
import com.springddd.domain.util.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

class GenProjectInfoControllerTest {

    private WebTestClient webTestClient;
    private GenProjectInfoQueryService queryService;
    private GenProjectInfoCommandService commandService;

    @BeforeEach
    void setUp() {
        queryService = mock(GenProjectInfoQueryService.class);
        commandService = mock(GenProjectInfoCommandService.class);
        GenProjectInfoController controller = new GenProjectInfoController(queryService, commandService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @DisplayName("POST /gen/projectInfo/index 应返回分页列表")
    void index_shouldReturnPage() {
        GenProjectInfoView view = new GenProjectInfoView();
        view.setId(1L);
        view.setTableName("sys_user");
        PageResponse<GenProjectInfoView> page = new PageResponse<>(
                List.of(view), 1L, 1, 10
        );
        when(queryService.index(any())).thenReturn(Mono.just(page));

        webTestClient.post()
                .uri("/gen/projectInfo/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"tableName\":\"sys_user\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data.items[0].tableName").isEqualTo("sys_user");
    }

    @Test
    @DisplayName("POST /gen/projectInfo/queryInfoByTableName 应返回项目信息")
    void queryInfoByTableName_shouldReturnInfo() {
        GenProjectInfoView view = new GenProjectInfoView();
        view.setTableName("sys_user");
        view.setClassName("SysUser");
        when(queryService.queryGenInfoByTableName("sys_user")).thenReturn(Mono.just(view));

        webTestClient.post()
                .uri("/gen/projectInfo/queryInfoByTableName?tableName=sys_user")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data.className").isEqualTo("SysUser");
    }

    @Test
    @DisplayName("POST /gen/projectInfo/create 应创建成功")
    void create_shouldSuccess() {
        when(commandService.create(any())).thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/gen/projectInfo/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"tableName\":\"sys_user\",\"className\":\"SysUser\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data").isEqualTo(1);
    }

    @Test
    @DisplayName("PUT /gen/projectInfo/update 应更新成功")
    void update_shouldSuccess() {
        when(commandService.update(any())).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/gen/projectInfo/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1,\"tableName\":\"updated\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /gen/projectInfo/delete 应删除成功")
    void delete_shouldSuccess() {
        when(commandService.delete(any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/gen/projectInfo/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("DELETE /gen/projectInfo/wipe 应彻底删除成功")
    void wipe_shouldSuccess() {
        when(commandService.wipeByIds(anyList())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/gen/projectInfo/wipe")
                        .queryParam("ids", 1, 2)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
