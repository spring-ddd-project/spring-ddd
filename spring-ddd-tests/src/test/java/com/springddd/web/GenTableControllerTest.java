package com.springddd.web;

import com.springddd.application.service.gen.GenTableInfoCommandService;
import com.springddd.application.service.gen.GenTableInfoQueryService;
import com.springddd.application.service.gen.dto.GenTableInfoPageQuery;
import com.springddd.application.service.gen.dto.GenTableInfoView;
import com.springddd.application.service.gen.dto.ProjectTreeView;
import com.springddd.domain.util.PageResponse;
import com.springddd.web.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(GenTableController.class)
@Import(TestSecurityConfig.class)
class GenTableControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenTableInfoQueryService genTableInfoQueryService;

    @MockBean
    private GenTableInfoCommandService genTableInfoCommandService;

    @Test
    void testIndex() {
        PageResponse<GenTableInfoView> pageResponse = new PageResponse<>(
                Collections.emptyList(), 0, 1, 10
        );
        when(genTableInfoQueryService.index(any(GenTableInfoPageQuery.class)))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.post()
                .uri("/gen/table/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"databaseName\":\"test_db\",\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testPreview() {
        ProjectTreeView view = new ProjectTreeView();
        view.setLabel("test.txt");
        view.setValue("content");

        when(genTableInfoQueryService.preview())
                .thenReturn(Mono.just(Arrays.asList(view)));

        webTestClient.post()
                .uri("/gen/table/preview")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testWipe() {
        when(genTableInfoCommandService.wipe())
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/gen/table/wipe")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testGenerate() {
        when(genTableInfoCommandService.generate(any()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/gen/table/generate?tableName=test_table")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
