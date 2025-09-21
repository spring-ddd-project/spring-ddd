package com.springddd.web;

import com.springddd.application.service.role.SysRoleMenuCommandService;
import com.springddd.application.service.role.SysRoleMenuQueryService;
import com.springddd.application.service.role.dto.SysRoleMenuView;
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

@WebFluxTest(SysRoleMenuController.class)
@Import(TestSecurityConfig.class)
class SysRoleMenuControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysRoleMenuQueryService sysRoleMenuQueryService;

    @MockBean
    private SysRoleMenuCommandService sysRoleMenuCommandService;

    @Test
    void testLinkRoleAndMenus() {
        when(sysRoleMenuCommandService.create(anyLong(), any()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/role/linkRoleAndMenus?roleId=1&menuIds=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testQueryMenusByRoleId() {
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(anyLong()))
                .thenReturn(Mono.just(Arrays.asList(new SysRoleMenuView(), new SysRoleMenuView())));

        webTestClient.post()
                .uri("/sys/role/queryMenusByRoleId?roleId=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testWipeLinkRoleAndMenus() {
        when(sysRoleMenuCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/sys/role/wipeLinkRoleAndMenus?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
