package com.springddd.web;

import com.springddd.application.service.role.SysRoleMenuCommandService;
import com.springddd.application.service.role.SysRoleMenuQueryService;
import com.springddd.application.service.role.dto.SysRoleMenuView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

class SysRoleMenuControllerTest {

    private WebTestClient webTestClient;
    private SysRoleMenuQueryService queryService;
    private SysRoleMenuCommandService commandService;

    @BeforeEach
    void setUp() {
        queryService = mock(SysRoleMenuQueryService.class);
        commandService = mock(SysRoleMenuCommandService.class);
        SysRoleMenuController controller = new SysRoleMenuController(queryService, commandService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @DisplayName("POST /sys/role/linkRoleAndMenus 应关联成功")
    void linkRoleAndMenus_shouldSuccess() {
        when(commandService.create(1L, List.of(2L, 3L))).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/role/linkRoleAndMenus?roleId=1&menuIds=2&menuIds=3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    @DisplayName("POST /sys/role/queryMenusByRoleId 应返回角色菜单列表")
    void queryMenusByRoleId_shouldReturnMenus() {
        SysRoleMenuView view = new SysRoleMenuView();
        view.setRoleId(1L);
        view.setMenuId(2L);
        when(queryService.queryLinkRoleAndMenus(1L)).thenReturn(Mono.just(List.of(view)));

        webTestClient.post()
                .uri("/sys/role/queryMenusByRoleId?roleId=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data[0].menuId").isEqualTo(2);
    }

    @Test
    @DisplayName("DELETE /sys/role/wipeLinkRoleAndMenus 应解除关联成功")
    void wipeLinkRoleAndMenus_shouldSuccess() {
        when(commandService.wipe(anyList())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/sys/role/wipeLinkRoleAndMenus")
                        .queryParam("ids", 1, 2)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
