package com.springddd.web;

import com.springddd.application.service.menu.SysMenuCommandService;
import com.springddd.application.service.menu.SysMenuQueryService;
import com.springddd.application.service.menu.dto.SysMenuCommand;
import com.springddd.application.service.menu.dto.SysMenuQuery;
import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.domain.util.ApiResponse;
import com.springddd.domain.util.PageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(SysMenuController.class)
class SysMenuControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysMenuQueryService sysMenuQueryService;

    @MockBean
    private SysMenuCommandService sysMenuCommandService;

    @Test
    @WithMockUser
    void index_shouldReturnOk() {
        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setMenuName("Test Menu");
        PageResponse<SysMenuView> pageResponse = new PageResponse<>(List.of(view), 1L, 1, 10);

        when(sysMenuQueryService.index(any(SysMenuQuery.class))).thenReturn(Mono.just(pageResponse));

        webTestClient.post().uri("/sys/menu/index")
                .bodyValue(new SysMenuQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void recycle_shouldReturnOk() {
        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setMenuName("Test Menu");
        PageResponse<SysMenuView> pageResponse = new PageResponse<>(List.of(view), 1L, 1, 10);

        when(sysMenuQueryService.recycle(any(SysMenuQuery.class))).thenReturn(Mono.just(pageResponse));

        webTestClient.post().uri("/sys/menu/recycle")
                .bodyValue(new SysMenuQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void all_shouldReturnOk() {
        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setMenuName("Test Menu");

        when(sysMenuQueryService.queryByPermissions()).thenReturn(Mono.just(List.of(view)));

        webTestClient.post().uri("/sys/menu/all")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void getMenuTreeWithoutPermission_shouldReturnOk() {
        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setMenuName("Test Menu");

        when(sysMenuQueryService.getMenuTreeWithoutPermission()).thenReturn(Mono.just(List.of(view)));

        webTestClient.post().uri("/sys/menu/getMenuTreeWithoutPermission")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void getMenuTreeWithPermission_shouldReturnOk() {
        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setMenuName("Test Menu");

        when(sysMenuQueryService.getMenuTreeWithPermission()).thenReturn(Mono.just(List.of(view)));

        webTestClient.post().uri("/sys/menu/getMenuTreeWithPermission")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void getByMenuId_shouldReturnOk() {
        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setMenuName("Test Menu");

        when(sysMenuQueryService.queryByMenuId(any())).thenReturn(Mono.just(view));

        webTestClient.post().uri("/sys/menu/getByMenuId?menuId=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void create_shouldReturnOk() {
        SysMenuCommand command = new SysMenuCommand();
        command.setMenuName("New Menu");
        command.setParentId(0L);

        when(sysMenuCommandService.create(any(SysMenuCommand.class))).thenReturn(Mono.just(1L));

        webTestClient.post().uri("/sys/menu/create")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void update_shouldReturnOk() {
        SysMenuCommand command = new SysMenuCommand();
        command.setId(1L);
        command.setMenuName("Updated Menu");

        when(sysMenuCommandService.update(any(SysMenuCommand.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/sys/menu/update")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void delete_shouldReturnOk() {
        when(sysMenuCommandService.delete(any())).thenReturn(Mono.empty());

        webTestClient.post().uri("/sys/menu/delete?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void restore_shouldReturnOk() {
        when(sysMenuCommandService.restore(any())).thenReturn(Mono.empty());

        webTestClient.post().uri("/sys/menu/restore?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void wipe_shouldReturnOk() {
        when(sysMenuCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete().uri("/sys/menu/wipe?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }
}
