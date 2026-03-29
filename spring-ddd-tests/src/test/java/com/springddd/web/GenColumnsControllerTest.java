package com.springddd.web;

import com.springddd.application.service.gen.GenColumnsCommandService;
import com.springddd.application.service.gen.GenColumnsQueryService;
import com.springddd.application.service.gen.dto.GenColumnsCommand;
import com.springddd.application.service.gen.dto.GenColumnsView;
import com.springddd.domain.util.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(GenColumnsController.class)
class GenColumnsControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenColumnsQueryService genColumnsQueryService;

    @MockBean
    private GenColumnsCommandService genColumnsCommandService;

    @Test
    @WithMockUser
    void getJavaEntityInfoByInfoId_shouldReturnOk() {
        GenColumnsView view = new GenColumnsView();
        view.setId(1L);
        view.setColumnName("test_column");

        when(genColumnsQueryService.queryJavaEntityInfoByInfoId(anyLong())).thenReturn(Mono.just(view));

        webTestClient.post().uri("/gen/columns/queryJavaEntityInfoByInfoId?infoId=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void getByInfoId_shouldReturnOk() {
        GenColumnsView view = new GenColumnsView();
        view.setId(1L);
        view.setColumnName("test_column");

        when(genColumnsQueryService.queryColumnsByGenInfoId(anyLong(), anyString())).thenReturn(Mono.just(List.of(view)));

        webTestClient.post().uri("/gen/columns/queryByInfoId?infoId=1&databaseName=testdb")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void create_shouldReturnOk() {
        GenColumnsCommand command = new GenColumnsCommand();
        command.setColumnName("new_column");

        when(genColumnsCommandService.create(any(GenColumnsCommand.class))).thenReturn(Mono.just(1L));

        webTestClient.post().uri("/gen/columns/create")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void batchCreate_shouldReturnOk() {
        GenColumnsCommand command = new GenColumnsCommand();
        command.setColumnName("new_column");

        when(genColumnsCommandService.batchSave(any())).thenReturn(Mono.just(List.of(1L, 2L)));

        webTestClient.post().uri("/gen/columns/batchCreate")
                .bodyValue(List.of(command))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void batchUpdate_shouldReturnOk() {
        GenColumnsCommand command = new GenColumnsCommand();
        command.setId(1L);
        command.setColumnName("updated_column");

        when(genColumnsCommandService.batchUpdate(any())).thenReturn(Mono.empty());

        webTestClient.put().uri("/gen/columns/batchUpdate")
                .bodyValue(List.of(command))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void update_shouldReturnOk() {
        GenColumnsCommand command = new GenColumnsCommand();
        command.setId(1L);
        command.setColumnName("updated_column");

        when(genColumnsCommandService.update(any(GenColumnsCommand.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/gen/columns/update")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void delete_shouldReturnOk() {
        GenColumnsCommand command = new GenColumnsCommand();
        command.setId(1L);

        when(genColumnsCommandService.delete(any(GenColumnsCommand.class))).thenReturn(Mono.empty());

        webTestClient.post().uri("/gen/columns/delete")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void wipe_shouldReturnOk() {
        when(genColumnsCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete().uri("/gen/columns/wipe?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }
}
