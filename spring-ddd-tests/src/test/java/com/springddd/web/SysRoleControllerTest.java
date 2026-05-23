package com.springddd.web;

import com.springddd.application.service.role.SysRoleCommandService;
import com.springddd.application.service.role.SysRoleQueryService;
import com.springddd.application.service.role.dto.SysRoleView;
import com.springddd.domain.util.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

class SysRoleControllerTest {

    private WebTestClient webTestClient;
    private SysRoleCommandService commandService;
    private SysRoleQueryService queryService;

    @BeforeEach
    void setUp() {
        commandService = mock(SysRoleCommandService.class);
        queryService = mock(SysRoleQueryService.class);
        SysRoleController controller = new SysRoleController(commandService, queryService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @DisplayName("POST /sys/role/index 应返回分页列表")
    void index_shouldReturnPage() {
        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setRoleName("admin");
        PageResponse<SysRoleView> page = new PageResponse<>(
                List.of(view), 1L, 1, 10
        );
        when(queryService.index(any())).thenReturn(Mono.just(page));

        webTestClient.post()
                .uri("/sys/role/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data.list[0].roleName").isEqualTo("admin");
    }

    @Test
    @DisplayName("POST /sys/role/recycle 应返回回收站分页")
    void recycle_shouldReturnRecyclePage() {
        SysRoleView view = new SysRoleView();
        view.setRoleName("deleted");
        PageResponse<SysRoleView> page = new PageResponse<>(
                List.of(view), 1L, 1, 10
        );
        when(queryService.recycle(any())).thenReturn(Mono.just(page));

        webTestClient.post()
                .uri("/sys/role/recycle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /sys/role/all 应返回所有角色")
    void all_shouldReturnAllRoles() {
        SysRoleView view = new SysRoleView();
        view.setRoleName("admin");
        when(queryService.getAllRole()).thenReturn(Mono.just(List.of(view)));

        webTestClient.post()
                .uri("/sys/role/all")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data[0].roleName").isEqualTo("admin");
    }

    @Test
    @DisplayName("POST /sys/role/create 应创建成功")
    void create_shouldSuccess() {
        when(commandService.createRole(any())).thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/sys/role/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"roleName\":\"newrole\",\"roleCode\":\"new\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data").isEqualTo(1);
    }

    @Test
    @DisplayName("PUT /sys/role/update 应更新成功")
    void update_shouldSuccess() {
        when(commandService.updateRole(any())).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/sys/role/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1,\"roleName\":\"updated\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /sys/role/delete 应删除成功")
    void delete_shouldSuccess() {
        when(commandService.deleteRole(anyList())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/role/delete?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /sys/role/restore 应恢复成功")
    void restore_shouldSuccess() {
        when(commandService.restore(anyList())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/role/restore?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("DELETE /sys/role/wipe 应彻底删除成功")
    void wipe_shouldSuccess() {
        when(commandService.wipe(anyList())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/sys/role/wipe")
                        .queryParam("ids", 1, 2)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
