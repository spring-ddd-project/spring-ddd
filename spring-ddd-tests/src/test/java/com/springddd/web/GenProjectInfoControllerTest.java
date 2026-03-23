package com.springddd.web;

import com.springddd.application.service.gen.GenProjectInfoCommandService;
import com.springddd.application.service.gen.GenProjectInfoQueryService;
import com.springddd.application.service.gen.dto.GenProjectInfoQuery;
import com.springddd.application.service.gen.dto.GenProjectInfoView;
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

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(GenProjectInfoController.class)
@Import(TestSecurityConfig.class)
class GenProjectInfoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenProjectInfoQueryService genProjectInfoQueryService;

    @MockBean
    private GenProjectInfoCommandService genProjectInfoCommandService;

    @Test
    void testIndex() {
        PageResponse<GenProjectInfoView> pageResponse = new PageResponse<>(
                Collections.emptyList(), 0, 0, 0
        );
        when(genProjectInfoQueryService.index(any(GenProjectInfoQuery.class)))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.post()
                .uri("/gen/projectInfo/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testGetInfo() {
        GenProjectInfoView view = new GenProjectInfoView();
        when(genProjectInfoQueryService.queryGenInfoByTableName(anyString()))
                .thenReturn(Mono.just(view));

        webTestClient.post()
                .uri("/gen/projectInfo/queryInfoByTableName?tableName=testTable")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testCreate() {
        when(genProjectInfoCommandService.create(any()))
                .thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/gen/projectInfo/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"tableName\":\"test_table\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testUpdate() {
        when(genProjectInfoCommandService.update(any()))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/gen/projectInfo/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testDelete() {
        when(genProjectInfoCommandService.delete(any()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/gen/projectInfo/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testWipe() {
        when(genProjectInfoCommandService.wipeByIds(any()))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/gen/projectInfo/wipe?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
