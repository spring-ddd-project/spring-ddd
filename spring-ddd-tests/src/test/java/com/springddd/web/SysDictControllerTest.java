package com.springddd.web;

import com.springddd.application.service.dict.SysDictCommandService;
import com.springddd.application.service.dict.SysDictQueryService;
import com.springddd.application.service.dict.dto.SysDictCommand;
import com.springddd.application.service.dict.dto.SysDictPageQuery;
import com.springddd.application.service.dict.dto.SysDictView;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(SysDictController.class)
class SysDictControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysDictQueryService sysDictQueryService;

    @MockBean
    private SysDictCommandService sysDictCommandService;

    @Test
    @WithMockUser
    void index_shouldReturnOk() {
        SysDictView view = new SysDictView();
        view.setId(1L);
        view.setDictName("Test Dict");
        PageResponse<SysDictView> pageResponse = new PageResponse<>(List.of(view), 1L, 1, 10);

        when(sysDictQueryService.index(any(SysDictPageQuery.class))).thenReturn(Mono.just(pageResponse));

        webTestClient.post().uri("/sys/dict/index")
                .bodyValue(new SysDictPageQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void getAll_shouldReturnOk() {
        SysDictView view = new SysDictView();
        view.setId(1L);
        view.setDictName("Test Dict");

        when(sysDictQueryService.queryAll()).thenReturn(Mono.just(List.of(view)));

        webTestClient.post().uri("/sys/dict/queryAll")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void getItemByDictCode_shouldReturnOk() {
        when(sysDictQueryService.queryDictByCode(anyString())).thenReturn(Mono.empty());

        webTestClient.post().uri("/sys/dict/queryItemByDictCode?dictCode=test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void recycle_shouldReturnOk() {
        SysDictView view = new SysDictView();
        view.setId(1L);
        view.setDictName("Test Dict");
        PageResponse<SysDictView> pageResponse = new PageResponse<>(List.of(view), 1L, 1, 10);

        when(sysDictQueryService.recycle(any(SysDictPageQuery.class))).thenReturn(Mono.just(pageResponse));

        webTestClient.post().uri("/sys/dict/recycle")
                .bodyValue(new SysDictPageQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void getItemLabel_shouldReturnOk() {
        when(sysDictQueryService.queryItemLabelByDictCode(anyString(), any())).thenReturn(Mono.just("label"));

        webTestClient.post().uri("/sys/dict/getItemLabel?dictCode=test&itemValue=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void create_shouldReturnOk() {
        SysDictCommand command = new SysDictCommand();
        command.setDictName("New Dict");
        command.setDictCode("new_dict");

        when(sysDictCommandService.create(any(SysDictCommand.class))).thenReturn(Mono.just(1L));

        webTestClient.post().uri("/sys/dict/create")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void update_shouldReturnOk() {
        SysDictCommand command = new SysDictCommand();
        command.setId(1L);
        command.setDictName("Updated Dict");

        when(sysDictCommandService.update(any(SysDictCommand.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/sys/dict/update")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void delete_shouldReturnOk() {
        when(sysDictCommandService.delete(any())).thenReturn(Mono.empty());

        webTestClient.post().uri("/sys/dict/delete?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void wipe_shouldReturnOk() {
        when(sysDictCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete().uri("/sys/dict/wipe?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void restore_shouldReturnOk() {
        when(sysDictCommandService.restore(any())).thenReturn(Mono.empty());

        webTestClient.post().uri("/sys/dict/restore?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }
}
