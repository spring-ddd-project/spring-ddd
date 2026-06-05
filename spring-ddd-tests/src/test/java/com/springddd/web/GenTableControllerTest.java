package com.springddd.web;

import com.springddd.application.service.gen.GenTableInfoCommandService;
import com.springddd.application.service.gen.GenTableInfoQueryService;
import com.springddd.application.service.gen.dto.GenTableInfoView;
import com.springddd.application.service.gen.dto.ProjectTreeView;
import com.springddd.domain.util.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

class GenTableControllerTest {

    private WebTestClient webTestClient;
    private GenTableInfoQueryService queryService;
    private GenTableInfoCommandService commandService;

    @BeforeEach
    void setUp() {
        queryService = mock(GenTableInfoQueryService.class);
        commandService = mock(GenTableInfoCommandService.class);
        GenTableController controller = new GenTableController(queryService, commandService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @DisplayName("POST /gen/table/index 应返回分页列表")
    void index_shouldReturnPage() {
        GenTableInfoView view = new GenTableInfoView();
        view.setTableName("sys_user");
        PageResponse<GenTableInfoView> page = new PageResponse<>(
                List.of(view), 1L, 1, 10
        );
        when(queryService.index(any())).thenReturn(Mono.just(page));

        webTestClient.post()
                .uri("/gen/table/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data.items[0].tableName").isEqualTo("sys_user");
    }

    @Test
    @DisplayName("POST /gen/table/preview 应返回预览数据")
    void preview_shouldReturnPreview() {
        ProjectTreeView view = new ProjectTreeView();
        view.setLabel("preview");
        when(queryService.preview()).thenReturn(Mono.just(List.of(view)));

        webTestClient.post()
                .uri("/gen/table/preview")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data[0].label").isEqualTo("preview");
    }

    @Test
    @DisplayName("GET /gen/table/download 应返回文件")
    void download_shouldReturnFile() {
        Resource resource = new ByteArrayResource("code content".getBytes());
        ResponseEntity<Resource> response = ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=code.zip")
                .body(resource);
        when(commandService.download()).thenReturn(Mono.just(response));

        webTestClient.get()
                .uri("/gen/table/download")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Disposition", "attachment; filename=code.zip");
    }

    @Test
    @DisplayName("DELETE /gen/table/wipe 应清空成功")
    void wipe_shouldSuccess() {
        when(commandService.wipe()).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/gen/table/wipe")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /gen/table/generate 应生成成功")
    void generate_shouldSuccess() {
        when(commandService.generate("sys_user")).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/gen/table/generate?tableName=sys_user")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /gen/table/generate 异常时应返回错误")
    void generate_shouldReturnError_whenException() {
        when(commandService.generate("sys_user")).thenReturn(Mono.error(new RuntimeException("generate failed")));

        webTestClient.post()
                .uri("/gen/table/generate?tableName=sys_user")
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
