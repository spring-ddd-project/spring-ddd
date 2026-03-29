package com.springddd.web;

import com.springddd.application.service.user.SysUserCommandService;
import com.springddd.application.service.user.SysUserQueryService;
import com.springddd.application.service.user.dto.SysUserCommand;
import com.springddd.application.service.user.dto.SysUserPageQuery;
import com.springddd.application.service.user.dto.SysUserView;
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

@WebFluxTest(SysUserController.class)
class SysUserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysUserQueryService sysUserQueryService;

    @MockBean
    private SysUserCommandService sysUserCommandService;

    @Test
    @WithMockUser
    void page_shouldReturnOk() {
        SysUserView view = new SysUserView();
        view.setId(1L);
        view.setUsername("testuser");
        PageResponse<SysUserView> pageResponse = new PageResponse<>(List.of(view), 1L, 1, 10);

        when(sysUserQueryService.page(any(SysUserPageQuery.class))).thenReturn(Mono.just(pageResponse));

        webTestClient.post().uri("/sys/user/index")
                .bodyValue(new SysUserPageQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void recyclePage_shouldReturnOk() {
        SysUserView view = new SysUserView();
        view.setId(1L);
        view.setUsername("testuser");
        PageResponse<SysUserView> pageResponse = new PageResponse<>(List.of(view), 1L, 1, 10);

        when(sysUserQueryService.recycle(any(SysUserPageQuery.class))).thenReturn(Mono.just(pageResponse));

        webTestClient.post().uri("/sys/user/recycle")
                .bodyValue(new SysUserPageQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void create_shouldReturnOk() {
        SysUserCommand command = new SysUserCommand();
        command.setUsername("newuser");
        command.setPassword("password");

        when(sysUserCommandService.createUser(any(SysUserCommand.class))).thenReturn(Mono.just(1L));

        webTestClient.post().uri("/sys/user/create")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void update_shouldReturnOk() {
        SysUserCommand command = new SysUserCommand();
        command.setId(1L);
        command.setUsername("updateduser");

        when(sysUserCommandService.updateUser(any(SysUserCommand.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/sys/user/update")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void delete_shouldReturnOk() {
        when(sysUserCommandService.delete(any())).thenReturn(Mono.empty());

        webTestClient.post().uri("/sys/user/delete?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void wipe_shouldReturnOk() {
        when(sysUserCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete().uri("/sys/user/wipe?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void restore_shouldReturnOk() {
        when(sysUserCommandService.restore(any())).thenReturn(Mono.empty());

        webTestClient.post().uri("/sys/user/restore?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }
}
