package com.springddd.web;

import com.springddd.application.service.user.SysUserCommandService;
import com.springddd.application.service.user.SysUserQueryService;
import com.springddd.application.service.user.dto.SysUserView;
import com.springddd.domain.util.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

class SysUserControllerTest {

    private WebTestClient webTestClient;
    private SysUserCommandService commandService;
    private SysUserQueryService queryService;

    @BeforeEach
    void setUp() {
        commandService = mock(SysUserCommandService.class);
        queryService = mock(SysUserQueryService.class);
        SysUserController controller = new SysUserController(commandService, queryService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @DisplayName("POST /sys/user/index 应返回分页列表")
    void index_shouldReturnPage() {
        SysUserView view = new SysUserView();
        view.setId(1L);
        view.setUsername("admin");
        PageResponse<SysUserView> page = new PageResponse<>(
                List.of(view), 1L, 1, 10
        );
        when(queryService.page(any())).thenReturn(Mono.just(page));

        webTestClient.post()
                .uri("/sys/user/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data.items[0].username").isEqualTo("admin");
    }

    @Test
    @DisplayName("POST /sys/user/recycle 应返回回收站分页")
    void recycle_shouldReturnRecyclePage() {
        SysUserView view = new SysUserView();
        view.setUsername("deleted");
        PageResponse<SysUserView> page = new PageResponse<>(
                List.of(view), 1L, 1, 10
        );
        when(queryService.recycle(any())).thenReturn(Mono.just(page));

        webTestClient.post()
                .uri("/sys/user/recycle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /sys/user/create 应创建成功")
    void create_shouldSuccess() {
        when(commandService.createUser(any())).thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/sys/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"username\":\"newuser\",\"password\":\"123456\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data").isEqualTo(1);
    }

    @Test
    @DisplayName("PUT /sys/user/update 应更新成功")
    void update_shouldSuccess() {
        when(commandService.updateUser(any())).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/sys/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1,\"username\":\"updated\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /sys/user/delete 应删除成功")
    void delete_shouldSuccess() {
        when(commandService.delete(anyList())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/user/delete?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("DELETE /sys/user/wipe 应彻底删除成功")
    void wipe_shouldSuccess() {
        when(commandService.wipe(anyList())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/sys/user/wipe")
                        .queryParam("ids", 1, 2)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /sys/user/restore 应恢复成功")
    void restore_shouldSuccess() {
        when(commandService.restore(anyList())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/user/restore?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
