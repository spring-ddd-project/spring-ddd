package com.springddd.web;

import com.springddd.application.service.gen.GenTemplateCommandService;
import com.springddd.application.service.gen.GenTemplateQueryService;
import com.springddd.application.service.gen.dto.GenTemplateCommand;
import com.springddd.application.service.gen.dto.GenTemplatePageQuery;
import com.springddd.application.service.gen.dto.GenTemplateView;
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

@WebFluxTest(GenTemplateController.class)
class GenTemplateControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenTemplateQueryService genTemplateQueryService;

    @MockBean
    private GenTemplateCommandService genTemplateCommandService;

    @Test
    @WithMockUser
    void index_shouldReturnOk() {
        GenTemplateView view = new GenTemplateView();
        view.setId(1L);
        view.setTemplateName("Test Template");
        PageResponse<GenTemplateView> pageResponse = new PageResponse<>(List.of(view), 1L, 1, 10);

        when(genTemplateQueryService.index(any(Mono.class))).thenReturn(Mono.just(pageResponse));

        webTestClient.post().uri("/gen/template/index")
                .bodyValue(new GenTemplatePageQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void recycle_shouldReturnOk() {
        GenTemplateView view = new GenTemplateView();
        view.setId(1L);
        view.setTemplateName("Test Template");
        PageResponse<GenTemplateView> pageResponse = new PageResponse<>(List.of(view), 1L, 1, 10);

        when(genTemplateQueryService.recycle(any(Mono.class))).thenReturn(Mono.just(pageResponse));

        webTestClient.post().uri("/gen/template/recycle")
                .bodyValue(new GenTemplatePageQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void create_shouldReturnOk() {
        GenTemplateCommand command = new GenTemplateCommand();
        command.setTemplateName("New Template");

        when(genTemplateCommandService.create(any(GenTemplateCommand.class))).thenReturn(Mono.just(1L));

        webTestClient.post().uri("/gen/template/create")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void update_shouldReturnOk() {
        GenTemplateCommand command = new GenTemplateCommand();
        command.setId(1L);
        command.setTemplateName("Updated Template");

        when(genTemplateCommandService.update(any(GenTemplateCommand.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/gen/template/update")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void delete_shouldReturnOk() {
        when(genTemplateCommandService.delete(any())).thenReturn(Mono.empty());

        webTestClient.post().uri("/gen/template/delete?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void restore_shouldReturnOk() {
        when(genTemplateCommandService.restore(any())).thenReturn(Mono.empty());

        webTestClient.post().uri("/gen/template/restore?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void wipe_shouldReturnOk() {
        when(genTemplateCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete().uri("/gen/template/wipe?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }
}
