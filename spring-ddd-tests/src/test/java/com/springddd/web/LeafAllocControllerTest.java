package com.springddd.web;

import com.springddd.application.service.leaf.LeafAllocCommandService;
import com.springddd.application.service.leaf.LeafAllocQueryService;
import com.springddd.application.service.leaf.dto.LeafAllocView;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.leaf.LeafSegmentIdGenerateDomainServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class LeafAllocControllerTest {

    private WebTestClient webTestClient;
    private LeafAllocCommandService commandService;
    private LeafAllocQueryService queryService;
    private LeafSegmentIdGenerateDomainServiceImpl segmentService;

    @BeforeEach
    void setUp() {
        commandService = mock(LeafAllocCommandService.class);
        queryService = mock(LeafAllocQueryService.class);
        segmentService = mock(LeafSegmentIdGenerateDomainServiceImpl.class);
        LeafAllocController controller = new LeafAllocController(commandService, queryService, segmentService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @DisplayName("POST /sys/leaf/index 应返回分页列表")
    void index_shouldReturnPage() {
        LeafAllocView view = new LeafAllocView();
        view.setId(1L);
        view.setBizTag("test");
        PageResponse<LeafAllocView> page = new PageResponse<>(
                List.of(view), 1L, 1, 10
        );
        when(queryService.page(any())).thenReturn(Mono.just(page));

        webTestClient.post()
                .uri("/sys/leaf/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data.items[0].bizTag").isEqualTo("test");
    }

    @Test
    @DisplayName("POST /sys/leaf/recycle 应返回回收站分页")
    void recycle_shouldReturnRecyclePage() {
        LeafAllocView view = new LeafAllocView();
        view.setBizTag("deleted");
        PageResponse<LeafAllocView> page = new PageResponse<>(
                List.of(view), 1L, 1, 10
        );
        when(queryService.recycle(any())).thenReturn(Mono.just(page));

        webTestClient.post()
                .uri("/sys/leaf/recycle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /sys/leaf/create 应创建成功")
    void create_shouldSuccess() {
        when(commandService.create(any())).thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/sys/leaf/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"bizTag\":\"test\",\"maxId\":100,\"step\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data").isEqualTo(1);
    }

    @Test
    @DisplayName("PUT /sys/leaf/update 应更新成功")
    void update_shouldSuccess() {
        when(commandService.update(any())).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/sys/leaf/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1,\"bizTag\":\"updated\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /sys/leaf/delete 应删除成功")
    void delete_shouldSuccess() {
        when(commandService.delete(anyLong())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/leaf/delete?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("DELETE /sys/leaf/wipe 应彻底删除成功")
    void wipe_shouldSuccess() {
        when(commandService.wipe(anyLong())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/sys/leaf/wipe")
                        .queryParam("ids", 1, 2)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /sys/leaf/restore 应恢复成功")
    void restore_shouldSuccess() {
        when(commandService.restore(anyLong())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/leaf/restore?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
