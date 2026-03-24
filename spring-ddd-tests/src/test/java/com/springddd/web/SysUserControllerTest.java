package com.springddd.web;

import com.springddd.application.service.user.SysUserCommandService;
import com.springddd.application.service.user.SysUserQueryService;
import com.springddd.application.service.user.dto.SysUserCommand;
import com.springddd.application.service.user.dto.SysUserPageQuery;
import com.springddd.application.service.user.dto.SysUserView;
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

@WebFluxTest(SysUserController.class)
@Import(TestSecurityConfig.class)
class SysUserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysUserQueryService sysUserQueryService;

    @MockBean
    private SysUserCommandService sysUserCommandService;

    @Test
    void testPage() {
        PageResponse<SysUserView> pageResponse = new PageResponse<>(
                Collections.emptyList(), 0, 1, 10
        );
        when(sysUserQueryService.page(any(SysUserPageQuery.class)))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.post()
                .uri("/sys/user/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testRecyclePage() {
        PageResponse<SysUserView> pageResponse = new PageResponse<>(
                Collections.emptyList(), 0, 1, 10
        );
        when(sysUserQueryService.recycle(any(SysUserPageQuery.class)))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.post()
                .uri("/sys/user/recycle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"pageNum\":1,\"pageSize\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testCreate() {
        when(sysUserCommandService.createUser(any(SysUserCommand.class)))
                .thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/sys/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"username\":\"testuser\",\"password\":\"pass123\",\"deptId\":1}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data").isEqualTo(1);
    }

    @Test
    void testCreateWithFullData() {
        when(sysUserCommandService.createUser(any(SysUserCommand.class)))
                .thenReturn(Mono.just(100L));

        webTestClient.post()
                .uri("/sys/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"username\":\"admin\",\"password\":\"admin123\",\"phone\":\"13800138000\",\"email\":\"admin@test.com\",\"deptId\":1,\"lockStatus\":false,\"avatar\":\"avatar.png\",\"sex\":true}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data").isEqualTo(100);
    }

    @Test
    void testUpdate() {
        when(sysUserCommandService.updateUser(any(SysUserCommand.class)))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/sys/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1,\"username\":\"updated\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testUpdateWithFullData() {
        when(sysUserCommandService.updateUser(any(SysUserCommand.class)))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/sys/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1,\"username\":\"admin\",\"phone\":\"13900139000\",\"email\":\"updated@test.com\",\"deptId\":2,\"lockStatus\":true}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testDelete() {
        when(sysUserCommandService.delete(any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/user/delete?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testDeleteSingleId() {
        when(sysUserCommandService.delete(any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/user/delete?ids=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testWipe() {
        when(sysUserCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/sys/user/wipe?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testWipeSingleId() {
        when(sysUserCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/sys/user/wipe?ids=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testRestore() {
        when(sysUserCommandService.restore(any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/user/restore?ids=1,2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testRestoreSingleId() {
        when(sysUserCommandService.restore(any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/user/restore?ids=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
