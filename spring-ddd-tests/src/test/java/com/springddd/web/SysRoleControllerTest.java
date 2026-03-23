package com.springddd.web;

import com.springddd.application.service.role.SysRoleCommandService;
import com.springddd.application.service.role.SysRoleQueryService;
import com.springddd.application.service.role.dto.SysRoleCommand;
import com.springddd.application.service.role.dto.SysRolePageQuery;
import com.springddd.application.service.role.dto.SysRoleView;
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
import static org.mockito.Mockito.when;

@WebFluxTest(SysRoleController.class)
@Import(TestSecurityConfig.class)
class SysRoleControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysRoleQueryService sysRoleQueryService;

    @MockBean
    private SysRoleCommandService sysRoleCommandService;

    @Test
    void testIndex() {
        PageResponse<SysRoleView> pageResponse = new PageResponse<>(
                Collections.emptyList(), 0, 1, 10
        );
        when(sysRoleQueryService.index(any(SysRolePageQuery.class)))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.post()
                .uri("/sys/role/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testRecycle() {
        PageResponse<SysRoleView> pageResponse = new PageResponse<>(
                Collections.emptyList(), 0, 1, 10
        );
        when(sysRoleQueryService.recycle(any(SysRolePageQuery.class)))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.post()
                .uri("/sys/role/recycle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testAll() {
        when(sysRoleQueryService.getAllRole()).thenReturn(Mono.just(Arrays.asList(new SysRoleView(), new SysRoleView())));

        webTestClient.post()
                .uri("/sys/role/all")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testCreate() {
        when(sysRoleCommandService.createRole(any(SysRoleCommand.class)))
                .thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/sys/role/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"roleName\":\"Test Role\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testUpdate() {
        when(sysRoleCommandService.updateRole(any(SysRoleCommand.class)))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/sys/role/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testDelete() {
        when(sysRoleCommandService.deleteRole(any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/role/delete?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testRestore() {
        when(sysRoleCommandService.restore(any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/role/restore?ids=1,2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testWipe() {
        when(sysRoleCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/sys/role/wipe?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
