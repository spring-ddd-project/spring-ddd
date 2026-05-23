package com.springddd.web;

import com.springddd.application.service.dept.SysDeptCommandService;
import com.springddd.application.service.dept.SysDeptQueryService;
import com.springddd.application.service.dept.dto.SysDeptView;
import com.springddd.domain.util.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

class SysDeptControllerTest {

    private WebTestClient webTestClient;
    private SysDeptQueryService queryService;
    private SysDeptCommandService commandService;

    @BeforeEach
    void setUp() {
        queryService = mock(SysDeptQueryService.class);
        commandService = mock(SysDeptCommandService.class);
        SysDeptController controller = new SysDeptController(queryService, commandService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @DisplayName("POST /sys/dept/index 应返回分页列表")
    void index_shouldReturnPage() {
        SysDeptView view = new SysDeptView();
        view.setId(1L);
        view.setDeptName("Tech");
        PageResponse<SysDeptView> page = new PageResponse<>(
                List.of(view), 1L, 1, 10
        );
        when(queryService.index(any())).thenReturn(Mono.just(page));

        webTestClient.post()
                .uri("/sys/dept/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"deptName\":\"Tech\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data.list[0].deptName").isEqualTo("Tech");
    }

    @Test
    @DisplayName("POST /sys/dept/recycle 应返回回收站分页")
    void recycle_shouldReturnRecyclePage() {
        SysDeptView view = new SysDeptView();
        view.setDeptName("Deleted");
        PageResponse<SysDeptView> page = new PageResponse<>(
                List.of(view), 1L, 1, 10
        );
        when(queryService.recycle(any())).thenReturn(Mono.just(page));

        webTestClient.post()
                .uri("/sys/dept/recycle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"deptName\":\"Deleted\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /sys/dept/tree 应返回部门树")
    void tree_shouldReturnTree() {
        SysDeptView view = new SysDeptView();
        view.setDeptName("Root");
        when(queryService.deptTree()).thenReturn(Mono.just(List.of(view)));

        webTestClient.post()
                .uri("/sys/dept/tree")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data[0].deptName").isEqualTo("Root");
    }

    @Test
    @DisplayName("POST /sys/dept/create 应创建成功")
    void create_shouldSuccess() {
        when(commandService.create(any())).thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/sys/dept/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"deptName\":\"New Dept\",\"parentId\":0}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data").isEqualTo(1);
    }

    @Test
    @DisplayName("PUT /sys/dept/update 应更新成功")
    void update_shouldSuccess() {
        when(commandService.update(any())).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/sys/dept/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1,\"deptName\":\"Updated\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /sys/dept/delete 应删除成功")
    void delete_shouldSuccess() {
        when(commandService.delete(anyList())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/dept/delete?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /sys/dept/restore 应恢复成功")
    void restore_shouldSuccess() {
        when(commandService.restore(anyList())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/dept/restore?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("DELETE /sys/dept/wipe 应彻底删除成功")
    void wipe_shouldSuccess() {
        when(commandService.wipe(anyList())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/sys/dept/wipe")
                        .queryParam("ids", 1, 2)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
