package com.springddd.web;

import com.springddd.application.service.dept.SysDeptCommandService;
import com.springddd.application.service.dept.SysDeptQueryService;
import com.springddd.application.service.dept.dto.SysDeptCommand;
import com.springddd.application.service.dept.dto.SysDeptQuery;
import com.springddd.application.service.dept.dto.SysDeptView;
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

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@WebFluxTest(SysDeptController.class)
@Import(TestSecurityConfig.class)
class SysDeptControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysDeptQueryService sysDeptQueryService;

    @MockBean
    private SysDeptCommandService sysDeptCommandService;

    @Test
    void testIndex() {
        PageResponse<SysDeptView> pageResponse = new PageResponse<>(
                Collections.emptyList(), 0, 0, 0
        );
        when(sysDeptQueryService.index(any(SysDeptQuery.class)))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.post()
                .uri("/sys/dept/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.message").isEqualTo("Success");
    }

    @Test
    void testRecycle() {
        PageResponse<SysDeptView> pageResponse = new PageResponse<>(
                Collections.emptyList(), 0, 0, 0
        );
        when(sysDeptQueryService.recycle(any(SysDeptQuery.class)))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.post()
                .uri("/sys/dept/recycle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testTree() {
        when(sysDeptQueryService.deptTree()).thenReturn(Mono.just(Arrays.asList(new SysDeptView(), new SysDeptView())));

        webTestClient.post()
                .uri("/sys/dept/tree")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testCreate() {
        when(sysDeptCommandService.create(any(SysDeptCommand.class)))
                .thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/sys/dept/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"deptName\":\"Test Dept\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testUpdate() {
        when(sysDeptCommandService.update(any(SysDeptCommand.class)))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/sys/dept/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1,\"deptName\":\"Updated Dept\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testDelete() {
        when(sysDeptCommandService.delete(anyList())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/dept/delete?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testRestore() {
        when(sysDeptCommandService.restore(anyList())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/dept/restore?ids=1,2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testWipe() {
        when(sysDeptCommandService.wipe(anyList())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/sys/dept/wipe?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
