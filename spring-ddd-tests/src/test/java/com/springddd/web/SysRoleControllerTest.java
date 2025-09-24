package com.springddd.web;

import com.springddd.application.service.role.SysRoleCommandService;
import com.springddd.application.service.role.SysRoleQueryService;
import com.springddd.application.service.role.dto.SysRoleCommand;
import com.springddd.application.service.role.dto.SysRolePageQuery;
import com.springddd.application.service.role.dto.SysRoleView;
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

@WebFluxTest(SysRoleController.class)
class SysRoleControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysRoleQueryService sysRoleQueryService;

    @MockBean
    private SysRoleCommandService sysRoleCommandService;

    @Test
    @WithMockUser
    void index_shouldReturnOk() {
        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setRoleName("Test Role");
        PageResponse<SysRoleView> pageResponse = new PageResponse<>(List.of(view), 1L, 1, 10);

        when(sysRoleQueryService.index(any(SysRolePageQuery.class))).thenReturn(Mono.just(pageResponse));

        webTestClient.post().uri("/sys/role/index")
                .bodyValue(new SysRolePageQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void recycle_shouldReturnOk() {
        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setRoleName("Test Role");
        PageResponse<SysRoleView> pageResponse = new PageResponse<>(List.of(view), 1L, 1, 10);

        when(sysRoleQueryService.recycle(any(SysRolePageQuery.class))).thenReturn(Mono.just(pageResponse));

        webTestClient.post().uri("/sys/role/recycle")
                .bodyValue(new SysRolePageQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void all_shouldReturnOk() {
        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setRoleName("Test Role");

        when(sysRoleQueryService.getAllRole()).thenReturn(Mono.just(List.of(view)));

        webTestClient.post().uri("/sys/role/all")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void create_shouldReturnOk() {
        SysRoleCommand command = new SysRoleCommand();
        command.setRoleName("New Role");

        when(sysRoleCommandService.createRole(any(SysRoleCommand.class))).thenReturn(Mono.just(1L));

        webTestClient.post().uri("/sys/role/create")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void update_shouldReturnOk() {
        SysRoleCommand command = new SysRoleCommand();
        command.setId(1L);
        command.setRoleName("Updated Role");

        when(sysRoleCommandService.updateRole(any(SysRoleCommand.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/sys/role/update")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void delete_shouldReturnOk() {
        when(sysRoleCommandService.deleteRole(any())).thenReturn(Mono.empty());

        webTestClient.post().uri("/sys/role/delete?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void restore_shouldReturnOk() {
        when(sysRoleCommandService.restore(any())).thenReturn(Mono.empty());

        webTestClient.post().uri("/sys/role/restore?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void wipe_shouldReturnOk() {
        when(sysRoleCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete().uri("/sys/role/wipe?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }
}
