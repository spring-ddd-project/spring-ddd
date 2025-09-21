package com.springddd.web;

import com.springddd.application.service.user.SysUserRoleCommandService;
import com.springddd.application.service.user.SysUserRoleQueryService;
import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.web.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebFluxTest(SysUserRoleController.class)
@Import(TestSecurityConfig.class)
class SysUserRoleControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysUserRoleQueryService sysUserRoleQueryService;

    @MockBean
    private SysUserRoleCommandService sysUserRoleCommandService;

    @Test
    void testQueryRolesByUserId() {
        when(sysUserRoleQueryService.queryLinkUserAndRole(anyLong()))
                .thenReturn(Mono.just(Arrays.asList(new SysUserRoleView(), new SysUserRoleView())));

        webTestClient.post()
                .uri("/sys/user/queryRolesByUserId?userId=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data.length()").isEqualTo(2);
    }

    @Test
    void testQueryRolesByUserIdWithEmptyResult() {
        when(sysUserRoleQueryService.queryLinkUserAndRole(anyLong()))
                .thenReturn(Mono.just(Collections.emptyList()));

        webTestClient.post()
                .uri("/sys/user/queryRolesByUserId?userId=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data.length()").isEqualTo(0);
    }

    @Test
    void testQueryRolesByUserIdWithSpecificUser() {
        SysUserRoleView roleView = new SysUserRoleView();
        roleView.setId(1L);
        roleView.setUserId(5L);
        roleView.setRoleId(10L);

        when(sysUserRoleQueryService.queryLinkUserAndRole(5L))
                .thenReturn(Mono.just(Arrays.asList(roleView)));

        webTestClient.post()
                .uri("/sys/user/queryRolesByUserId?userId=5")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data[0].userId").isEqualTo(5)
                .jsonPath("$.data[0].roleId").isEqualTo(10);
    }

    @Test
    void testLinkUserAndRole() {
        when(sysUserRoleCommandService.create(anyLong(), any()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/user/linkUserAndRole?userId=1&roleIds=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testLinkUserAndRoleWithSingleRole() {
        when(sysUserRoleCommandService.create(anyLong(), any()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/user/linkUserAndRole?userId=1&roleIds=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testLinkUserAndRoleWithManyRoles() {
        when(sysUserRoleCommandService.create(anyLong(), any()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/user/linkUserAndRole?userId=1&roleIds=1,2,3,4,5")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testWipeLinkUserAndRole() {
        when(sysUserRoleCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/sys/user/wipeLinkUserAndRole?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testWipeLinkUserAndRoleWithSingleId() {
        when(sysUserRoleCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/sys/user/wipeLinkUserAndRole?ids=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testWipeLinkUserAndRoleWithManyIds() {
        when(sysUserRoleCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/sys/user/wipeLinkUserAndRole?ids=1,2,3,4,5,6,7,8,9,10")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
