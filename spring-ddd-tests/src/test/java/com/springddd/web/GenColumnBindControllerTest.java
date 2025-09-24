package com.springddd.web;

import com.springddd.application.service.gen.GenColumnBindCommandService;
import com.springddd.application.service.gen.GenColumnBindQueryService;
import com.springddd.application.service.gen.dto.GenColumnBindCommand;
import com.springddd.application.service.gen.dto.GenColumnBindPageQuery;
import com.springddd.application.service.gen.dto.GenColumnBindView;
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

@WebFluxTest(GenColumnBindController.class)
class GenColumnBindControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenColumnBindQueryService genColumnBindQueryService;

    @MockBean
    private GenColumnBindCommandService genColumnBindCommandService;

    @Test
    @WithMockUser
    void index_shouldReturnOk() {
        GenColumnBindView view = new GenColumnBindView();
        view.setId(1L);
        PageResponse<GenColumnBindView> pageResponse = new PageResponse<>(List.of(view), 1L, 1, 10);

        when(genColumnBindQueryService.index(any(Mono.class))).thenReturn(Mono.just(pageResponse));

        webTestClient.post().uri("/gen/column/bind/index")
                .bodyValue(new GenColumnBindPageQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void recycle_shouldReturnOk() {
        GenColumnBindView view = new GenColumnBindView();
        view.setId(1L);
        PageResponse<GenColumnBindView> pageResponse = new PageResponse<>(List.of(view), 1L, 1, 10);

        when(genColumnBindQueryService.recycle(any(Mono.class))).thenReturn(Mono.just(pageResponse));

        webTestClient.post().uri("/gen/column/bind/recycle")
                .bodyValue(new GenColumnBindPageQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void create_shouldReturnOk() {
        GenColumnBindCommand command = new GenColumnBindCommand();
        command.setColumnName("test_column");

        when(genColumnBindCommandService.create(any(GenColumnBindCommand.class))).thenReturn(Mono.just(1L));

        webTestClient.post().uri("/gen/column/bind/create")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void update_shouldReturnOk() {
        GenColumnBindCommand command = new GenColumnBindCommand();
        command.setId(1L);
        command.setColumnName("updated_column");

        when(genColumnBindCommandService.update(any(GenColumnBindCommand.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/gen/column/bind/update")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void delete_shouldReturnOk() {
        when(genColumnBindCommandService.delete(any())).thenReturn(Mono.empty());

        webTestClient.post().uri("/gen/column/bind/delete?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void wipe_shouldReturnOk() {
        when(genColumnBindCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete().uri("/gen/column/bind/wipe?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void restore_shouldReturnOk() {
        when(genColumnBindCommandService.restore(any())).thenReturn(Mono.empty());

        webTestClient.post().uri("/gen/column/bind/restore?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }
}
