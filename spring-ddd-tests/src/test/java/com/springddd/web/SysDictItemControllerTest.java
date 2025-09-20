package com.springddd.web;

import com.springddd.application.service.dict.SysDictItemCommandService;
import com.springddd.application.service.dict.SysDictItemQueryService;
import com.springddd.application.service.dict.dto.SysDictItemCommand;
import com.springddd.application.service.dict.dto.SysDictItemPageQuery;
import com.springddd.application.service.dict.dto.SysDictItemView;
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
import static org.mockito.Mockito.when;

@WebFluxTest(SysDictItemController.class)
@Import(TestSecurityConfig.class)
class SysDictItemControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysDictItemQueryService sysDictItemQueryService;

    @MockBean
    private SysDictItemCommandService sysDictItemCommandService;

    @Test
    void testIndex() {
        PageResponse<SysDictItemView> pageResponse = new PageResponse<>(
                Collections.emptyList(), 0, 1, 10
        );
        when(sysDictItemQueryService.index(any(SysDictItemPageQuery.class)))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.post()
                .uri("/sys/dict/item/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testRecycle() {
        PageResponse<SysDictItemView> pageResponse = new PageResponse<>(
                Collections.emptyList(), 0, 1, 10
        );
        when(sysDictItemQueryService.recycle(any(SysDictItemPageQuery.class)))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.post()
                .uri("/sys/dict/item/recycle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testCreate() {
        when(sysDictItemCommandService.create(any(SysDictItemCommand.class)))
                .thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/sys/dict/item/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"itemLabel\":\"Test Item\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testUpdate() {
        when(sysDictItemCommandService.update(any(SysDictItemCommand.class)))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/sys/dict/item/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testDelete() {
        when(sysDictItemCommandService.delete(any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/dict/item/delete?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testRestore() {
        when(sysDictItemCommandService.restore(any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/dict/item/restore?ids=1,2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testWipe() {
        when(sysDictItemCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/sys/dict/item/wipe?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
