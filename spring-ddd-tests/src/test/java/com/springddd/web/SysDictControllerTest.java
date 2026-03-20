package com.springddd.web;

import com.springddd.application.service.dict.SysDictCommandService;
import com.springddd.application.service.dict.SysDictQueryService;
import com.springddd.application.service.dict.dto.SysDictCommand;
import com.springddd.application.service.dict.dto.SysDictPageQuery;
import com.springddd.application.service.dict.dto.SysDictView;
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

@WebFluxTest(SysDictController.class)
@Import(TestSecurityConfig.class)
class SysDictControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysDictQueryService sysDictQueryService;

    @MockBean
    private SysDictCommandService sysDictCommandService;

    @Test
    void testIndex() {
        PageResponse<SysDictView> pageResponse = new PageResponse<>(
                Collections.emptyList(), 0, 1, 10
        );
        when(sysDictQueryService.index(any(SysDictPageQuery.class)))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.post()
                .uri("/sys/dict/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testGetAll() {
        when(sysDictQueryService.queryAll()).thenReturn(Mono.just(Collections.emptyList()));

        webTestClient.post()
                .uri("/sys/dict/queryAll")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testGetItemByDictCode() {
        when(sysDictQueryService.queryDictByCode(anyString()))
                .thenReturn(Mono.just(Collections.emptyList()));

        webTestClient.post()
                .uri("/sys/dict/queryItemByDictCode?dictCode=testCode")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testRecycle() {
        PageResponse<SysDictView> pageResponse = new PageResponse<>(
                Collections.emptyList(), 0, 1, 10
        );
        when(sysDictQueryService.recycle(any(SysDictPageQuery.class)))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.post()
                .uri("/sys/dict/recycle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testGetItemLabel() {
        when(sysDictQueryService.queryItemLabelByDictCode(anyString(), any()))
                .thenReturn(Mono.just("label"));

        webTestClient.post()
                .uri("/sys/dict/getItemLabel?dictCode=testCode&itemValue=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testCreate() {
        when(sysDictCommandService.create(any(SysDictCommand.class)))
                .thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/sys/dict/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"dictName\":\"Test Dict\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testUpdate() {
        when(sysDictCommandService.update(any(SysDictCommand.class)))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/sys/dict/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testDelete() {
        when(sysDictCommandService.delete(any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/dict/delete?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testWipe() {
        when(sysDictCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/sys/dict/wipe?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testRestore() {
        when(sysDictCommandService.restore(any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/dict/restore?ids=1,2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
