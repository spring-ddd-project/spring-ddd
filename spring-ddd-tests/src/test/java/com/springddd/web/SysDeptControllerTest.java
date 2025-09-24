package com.springddd.web;

import com.springddd.application.service.dept.SysDeptCommandService;
import com.springddd.application.service.dept.SysDeptQueryService;
import com.springddd.application.service.dept.dto.SysDeptCommand;
import com.springddd.application.service.dept.dto.SysDeptQuery;
import com.springddd.application.service.dept.dto.SysDeptView;
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

@WebFluxTest(SysDeptController.class)
class SysDeptControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysDeptQueryService sysDeptQueryService;

    @MockBean
    private SysDeptCommandService sysDeptCommandService;

    @Test
    @WithMockUser
    void index_shouldReturnOk() {
        SysDeptView view = new SysDeptView();
        view.setId(1L);
        view.setDeptName("Test Dept");
        PageResponse<SysDeptView> pageResponse = new PageResponse<>(List.of(view), 1L, 1, 10);

        when(sysDeptQueryService.index(any(SysDeptQuery.class))).thenReturn(Mono.just(pageResponse));

        webTestClient.post().uri("/sys/dept/index")
                .bodyValue(new SysDeptQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void recycle_shouldReturnOk() {
        SysDeptView view = new SysDeptView();
        view.setId(1L);
        view.setDeptName("Test Dept");
        PageResponse<SysDeptView> pageResponse = new PageResponse<>(List.of(view), 1L, 1, 10);

        when(sysDeptQueryService.recycle(any(SysDeptQuery.class))).thenReturn(Mono.just(pageResponse));

        webTestClient.post().uri("/sys/dept/recycle")
                .bodyValue(new SysDeptQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void tree_shouldReturnOk() {
        SysDeptView view = new SysDeptView();
        view.setId(1L);
        view.setDeptName("Test Dept");

        when(sysDeptQueryService.deptTree()).thenReturn(Mono.just(List.of(view)));

        webTestClient.post().uri("/sys/dept/tree")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void create_shouldReturnOk() {
        SysDeptCommand command = new SysDeptCommand();
        command.setDeptName("New Dept");
        command.setParentId(0L);
        command.setSortOrder(1);
        command.setDeptStatus(true);

        when(sysDeptCommandService.create(any(SysDeptCommand.class))).thenReturn(Mono.just(1L));

        webTestClient.post().uri("/sys/dept/create")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void update_shouldReturnOk() {
        SysDeptCommand command = new SysDeptCommand();
        command.setId(1L);
        command.setDeptName("Updated Dept");
        command.setParentId(0L);
        command.setSortOrder(1);
        command.setDeptStatus(true);

        when(sysDeptCommandService.update(any(SysDeptCommand.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/sys/dept/update")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void delete_shouldReturnOk() {
        when(sysDeptCommandService.delete(any())).thenReturn(Mono.empty());

        webTestClient.post().uri("/sys/dept/delete?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void restore_shouldReturnOk() {
        when(sysDeptCommandService.restore(any())).thenReturn(Mono.empty());

        webTestClient.post().uri("/sys/dept/restore?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void wipe_shouldReturnOk() {
        when(sysDeptCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete().uri("/sys/dept/wipe?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }
}
