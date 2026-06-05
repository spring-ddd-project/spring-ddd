package com.springddd.web;

import com.springddd.application.service.menu.SysMenuCommandService;
import com.springddd.application.service.menu.SysMenuQueryService;
import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.domain.util.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

class SysMenuControllerTest {

    private WebTestClient webTestClient;
    private SysMenuQueryService queryService;
    private SysMenuCommandService commandService;

    @BeforeEach
    void setUp() {
        queryService = mock(SysMenuQueryService.class);
        commandService = mock(SysMenuCommandService.class);
        SysMenuController controller = new SysMenuController(queryService, commandService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @DisplayName("POST /sys/menu/index 应返回分页列表")
    void index_shouldReturnPage() {
        SysMenuView view = new SysMenuView();
        view.setName("System");
        PageResponse<SysMenuView> page = new PageResponse<>(
                List.of(view), 1L, 1, 10
        );
        when(queryService.index(any())).thenReturn(Mono.just(page));

        webTestClient.post()
                .uri("/sys/menu/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\":\"System\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data.items[0].name").isEqualTo("System");
    }

    @Test
    @DisplayName("POST /sys/menu/recycle 应返回回收站分页")
    void recycle_shouldReturnRecyclePage() {
        SysMenuView view = new SysMenuView();
        view.setName("Deleted Menu");
        PageResponse<SysMenuView> page = new PageResponse<>(
                List.of(view), 1L, 1, 10
        );
        when(queryService.recycle(any())).thenReturn(Mono.just(page));

        webTestClient.post()
                .uri("/sys/menu/recycle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\":\"Deleted Menu\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /sys/menu/all 应返回所有菜单")
    void all_shouldReturnAllMenus() {
        SysMenuView view = new SysMenuView();
        view.setName("dashboard");
        when(queryService.queryByPermissions()).thenReturn(Mono.just(List.of(view)));

        webTestClient.post()
                .uri("/sys/menu/all")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data[0].name").isEqualTo("dashboard");
    }

    @Test
    @DisplayName("POST /sys/menu/getMenuTreeWithoutPermission 应返回菜单树")
    void getMenuTreeWithoutPermission_shouldReturnTree() {
        SysMenuView view = new SysMenuView();
        view.setName("root");
        when(queryService.getMenuTreeWithoutPermission()).thenReturn(Mono.just(List.of(view)));

        webTestClient.post()
                .uri("/sys/menu/getMenuTreeWithoutPermission")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data[0].name").isEqualTo("root");
    }

    @Test
    @DisplayName("POST /sys/menu/getMenuTreeWithPermission 应返回带权限菜单树")
    void getMenuTreeWithPermission_shouldReturnTree() {
        SysMenuView view = new SysMenuView();
        view.setName("root");
        when(queryService.getMenuTreeWithPermission()).thenReturn(Mono.just(List.of(view)));

        webTestClient.post()
                .uri("/sys/menu/getMenuTreeWithPermission")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /sys/menu/getByMenuId 应返回菜单详情")
    void getByMenuId_shouldReturnMenu() {
        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setName("System");
        when(queryService.queryByMenuId(1L)).thenReturn(Mono.just(view));

        webTestClient.post()
                .uri("/sys/menu/getByMenuId?menuId=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data.name").isEqualTo("System");
    }

    @Test
    @DisplayName("POST /sys/menu/create 应创建成功")
    void create_shouldSuccess() {
        when(commandService.create(any())).thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/sys/menu/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\":\"New Menu\",\"path\":\"/new\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data").isEqualTo(1);
    }

    @Test
    @DisplayName("PUT /sys/menu/update 应更新成功")
    void update_shouldSuccess() {
        when(commandService.update(any())).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/sys/menu/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1,\"name\":\"Updated Menu\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /sys/menu/delete 应删除成功")
    void delete_shouldSuccess() {
        when(commandService.delete(anyList())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/menu/delete?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /sys/menu/restore 应恢复成功")
    void restore_shouldSuccess() {
        when(commandService.restore(anyList())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/menu/restore?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("DELETE /sys/menu/wipe 应彻底删除成功")
    void wipe_shouldSuccess() {
        when(commandService.wipe(anyList())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/sys/menu/wipe")
                        .queryParam("ids", 1, 2)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
