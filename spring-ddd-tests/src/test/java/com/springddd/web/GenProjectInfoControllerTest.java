package com.springddd.web;

import com.springddd.application.service.gen.GenProjectInfoCommandService;
import com.springddd.application.service.gen.GenProjectInfoQueryService;
import com.springddd.application.service.gen.dto.GenProjectInfoCommand;
import com.springddd.application.service.gen.dto.GenProjectInfoQuery;
import com.springddd.application.service.gen.dto.GenProjectInfoView;
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

@WebFluxTest(GenProjectInfoController.class)
class GenProjectInfoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenProjectInfoQueryService genProjectInfoQueryService;

    @MockBean
    private GenProjectInfoCommandService genProjectInfoCommandService;

    @Test
    @WithMockUser
    void index_shouldReturnOk() {
        GenProjectInfoView view = new GenProjectInfoView();
        view.setId(1L);
        view.setProjectName("Test Project");
        PageResponse<GenProjectInfoView> pageResponse = new PageResponse<>(List.of(view), 1L, 1, 10);

        when(genProjectInfoQueryService.index(any(GenProjectInfoQuery.class))).thenReturn(Mono.just(pageResponse));

        webTestClient.post().uri("/gen/projectInfo/index")
                .bodyValue(new GenProjectInfoQuery())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void getInfo_shouldReturnOk() {
        GenProjectInfoView view = new GenProjectInfoView();
        view.setId(1L);
        view.setProjectName("Test Project");

        when(genProjectInfoQueryService.queryGenInfoByTableName(anyString())).thenReturn(Mono.just(view));

        webTestClient.post().uri("/gen/projectInfo/queryInfoByTableName?tableName=test_table")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void create_shouldReturnOk() {
        GenProjectInfoCommand command = new GenProjectInfoCommand();
        command.setProjectName("New Project");

        when(genProjectInfoCommandService.create(any(GenProjectInfoCommand.class))).thenReturn(Mono.just(1L));

        webTestClient.post().uri("/gen/projectInfo/create")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void update_shouldReturnOk() {
        GenProjectInfoCommand command = new GenProjectInfoCommand();
        command.setId(1L);
        command.setProjectName("Updated Project");

        when(genProjectInfoCommandService.update(any(GenProjectInfoCommand.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/gen/projectInfo/update")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void delete_shouldReturnOk() {
        GenProjectInfoCommand command = new GenProjectInfoCommand();
        command.setId(1L);

        when(genProjectInfoCommandService.delete(any(GenProjectInfoCommand.class))).thenReturn(Mono.empty());

        webTestClient.post().uri("/gen/projectInfo/delete")
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }

    @Test
    @WithMockUser
    void wipe_shouldReturnOk() {
        when(genProjectInfoCommandService.wipeByIds(any())).thenReturn(Mono.empty());

        webTestClient.delete().uri("/gen/projectInfo/wipe?ids=1&ids=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class);
    }
}
