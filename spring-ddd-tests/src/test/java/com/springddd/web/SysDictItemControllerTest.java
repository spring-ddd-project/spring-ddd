package com.springddd.web;

import com.springddd.application.service.dict.SysDictItemCommandService;
import com.springddd.application.service.dict.SysDictItemQueryService;
import com.springddd.application.service.dict.dto.SysDictItemCommand;
import com.springddd.application.service.dict.dto.SysDictItemPageQuery;
import com.springddd.application.service.dict.dto.SysDictItemView;
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

@WebFluxTest(SysDictItemController.class)
class SysDictItemControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysDictItemQueryService sysDictItemQueryService;

    @MockBean
    private SysDictItemCommandService sysDictItemCommandService;

    @Test
    @WithMockUser
    void index_shouldReturnOk() {
        SysDictItemView view = new SysDictItemView();
        view.setId(1L);
        view.setItemText("Test Item");
        PageResponse<SysDictItemView> pageResponse = new PageResponse<>(List.of(view), 1L, 1, 10);

        when(sysDictItemQueryService.index(any(Mono.class))).thenReturn(Mono.just(pageResponse));

        webTestClient.post().uri("/sys/dict/item/index")
                .bodyValue(new SysDictItemPageQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void recycle_shouldReturnOk() {
        SysDictItemView view = new SysDictItemView();
        view.setId(1L);
        view.setItemText("Test Item");
        PageResponse<SysDictItemView> pageResponse = new PageResponse<>(List.of(view), 1L, 1, 10);

        when(sysDictItemQueryService.recycle(any(Mono.class))).thenReturn(Mono.just(pageResponse));

        webTestClient.post().uri("/sys/dict/item/recycle")
                .bodyValue(new SysDictItemPageQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void create_shouldReturnOk() {
        SysDictItemCommand command = new SysDictItemCommand();
        command.setItemText("New Item");
        command.setItemValue(1);

        when(sysDictItemCommandService.create(any(SysDictItemCommand.class))).thenReturn(Mono.just(1L));

        webTestClient.post().uri("/sys/dict/item/create")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void update_shouldReturnOk() {
        SysDictItemCommand command = new SysDictItemCommand();
        command.setId(1L);
        command.setItemText("Updated Item");

        when(sysDictItemCommandService.update(any(SysDictItemCommand.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/sys/dict/item/update")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void delete_shouldReturnOk() {
        when(sysDictItemCommandService.delete(any())).thenReturn(Mono.empty());

        webTestClient.post().uri("/sys/dict/item/delete?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void restore_shouldReturnOk() {
        when(sysDictItemCommandService.restore(any())).thenReturn(Mono.empty());

        webTestClient.post().uri("/sys/dict/item/restore?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void wipe_shouldReturnOk() {
        when(sysDictItemCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete().uri("/sys/dict/item/wipe?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }
}
