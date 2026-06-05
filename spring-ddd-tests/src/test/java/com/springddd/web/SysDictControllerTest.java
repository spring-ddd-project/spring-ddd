package com.springddd.web;

import com.springddd.application.service.dict.SysDictCommandService;
import com.springddd.application.service.dict.SysDictQueryService;
import com.springddd.application.service.dict.dto.SysDictItemView;
import com.springddd.application.service.dict.dto.SysDictView;
import com.springddd.domain.util.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

class SysDictControllerTest {

    private WebTestClient webTestClient;
    private SysDictCommandService commandService;
    private SysDictQueryService queryService;

    @BeforeEach
    void setUp() {
        commandService = mock(SysDictCommandService.class);
        queryService = mock(SysDictQueryService.class);
        SysDictController controller = new SysDictController(commandService, queryService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @DisplayName("POST /sys/dict/index 应返回分页列表")
    void index_shouldReturnPage() {
        SysDictView view = new SysDictView();
        view.setId(1L);
        view.setDictCode("status");
        PageResponse<SysDictView> page = new PageResponse<>(
                List.of(view), 1L, 1, 10
        );
        when(queryService.index(any())).thenReturn(Mono.just(page));

        webTestClient.post()
                .uri("/sys/dict/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data.items[0].dictCode").isEqualTo("status");
    }

    @Test
    @DisplayName("POST /sys/dict/queryAll 应返回所有字典")
    void queryAll_shouldReturnAll() {
        SysDictView view = new SysDictView();
        view.setDictCode("gender");
        when(queryService.queryAll()).thenReturn(Mono.just(List.of(view)));

        webTestClient.post()
                .uri("/sys/dict/queryAll")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data[0].dictCode").isEqualTo("gender");
    }

    @Test
    @DisplayName("POST /sys/dict/queryItemByDictCode 应返回字典项")
    void queryItemByDictCode_shouldReturnItems() {
        SysDictItemView item = new SysDictItemView();
        item.setItemLabel("male");
        when(queryService.queryDictByCode("gender")).thenReturn(Mono.just(List.of(item)));

        webTestClient.post()
                .uri("/sys/dict/queryItemByDictCode?dictCode=gender")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data[0].itemLabel").isEqualTo("male");
    }

    @Test
    @DisplayName("POST /sys/dict/recycle 应返回回收站分页")
    void recycle_shouldReturnRecyclePage() {
        SysDictView view = new SysDictView();
        view.setDictCode("deleted");
        PageResponse<SysDictView> page = new PageResponse<>(
                List.of(view), 1L, 1, 10
        );
        when(queryService.recycle(any())).thenReturn(Mono.just(page));

        webTestClient.post()
                .uri("/sys/dict/recycle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /sys/dict/getItemLabel 应返回标签")
    void getItemLabel_shouldReturnLabel() {
        when(queryService.queryItemLabelByDictCode("gender", 1)).thenReturn(Mono.just("male"));

        webTestClient.post()
                .uri("/sys/dict/getItemLabel?dictCode=gender&itemValue=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data").isEqualTo("male");
    }

    @Test
    @DisplayName("POST /sys/dict/create 应创建成功")
    void create_shouldSuccess() {
        when(commandService.create(any())).thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/sys/dict/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"dictCode\":\"test\",\"dictName\":\"Test Dict\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data").isEqualTo(1);
    }

    @Test
    @DisplayName("PUT /sys/dict/update 应更新成功")
    void update_shouldSuccess() {
        when(commandService.update(any())).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/sys/dict/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1,\"dictCode\":\"updated\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /sys/dict/delete 应删除成功")
    void delete_shouldSuccess() {
        when(commandService.delete(anyList())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/dict/delete?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("DELETE /sys/dict/wipe 应彻底删除成功")
    void wipe_shouldSuccess() {
        when(commandService.wipe(anyList())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/sys/dict/wipe")
                        .queryParam("ids", 1, 2)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /sys/dict/restore 应恢复成功")
    void restore_shouldSuccess() {
        when(commandService.restore(anyList())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/dict/restore?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
