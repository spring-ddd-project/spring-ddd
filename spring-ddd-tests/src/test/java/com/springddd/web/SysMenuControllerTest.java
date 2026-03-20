package com.springddd.web;

import com.springddd.application.service.menu.SysMenuCommandService;
import com.springddd.application.service.menu.SysMenuQueryService;
import com.springddd.application.service.menu.dto.SysMenuCommand;
import com.springddd.application.service.menu.dto.SysMenuQuery;
import com.springddd.application.service.menu.dto.SysMenuView;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebFluxTest(SysMenuController.class)
@Import(TestSecurityConfig.class)
class SysMenuControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SysMenuQueryService sysMenuQueryService;

    @MockBean
    private SysMenuCommandService sysMenuCommandService;

    @Test
    void testIndex() {
        PageResponse<SysMenuView> pageResponse = new PageResponse<>(
                Collections.emptyList(), 0, 0, 0
        );
        when(sysMenuQueryService.index(any(SysMenuQuery.class)))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.post()
                .uri("/sys/menu/index")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testRecycle() {
        PageResponse<SysMenuView> pageResponse = new PageResponse<>(
                Collections.emptyList(), 0, 0, 0
        );
        when(sysMenuQueryService.recycle(any(SysMenuQuery.class)))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.post()
                .uri("/sys/menu/recycle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testAll() {
        when(sysMenuQueryService.queryByPermissions())
                .thenReturn(Mono.just(Arrays.asList(new SysMenuView(), new SysMenuView())));

        webTestClient.post()
                .uri("/sys/menu/all")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testGetMenuTreeWithoutPermission() {
        when(sysMenuQueryService.getMenuTreeWithoutPermission())
                .thenReturn(Mono.just(Arrays.asList(new SysMenuView(), new SysMenuView())));

        webTestClient.post()
                .uri("/sys/menu/getMenuTreeWithoutPermission")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testGetMenuTreeWithPermission() {
        when(sysMenuQueryService.getMenuTreeWithPermission())
                .thenReturn(Mono.just(Arrays.asList(new SysMenuView())));

        webTestClient.post()
                .uri("/sys/menu/getMenuTreeWithPermission")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testGetByMenuId() {
        when(sysMenuQueryService.queryByMenuId(anyLong()))
                .thenReturn(Mono.just(new SysMenuView()));

        webTestClient.post()
                .uri("/sys/menu/getByMenuId?menuId=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testCreate() {
        when(sysMenuCommandService.create(any(SysMenuCommand.class)))
                .thenReturn(Mono.just(1L));

        webTestClient.post()
                .uri("/sys/menu/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\":\"Test Menu\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testUpdate() {
        when(sysMenuCommandService.update(any(SysMenuCommand.class)))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/sys/menu/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testDelete() {
        when(sysMenuCommandService.delete(any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/menu/delete?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testRestore() {
        when(sysMenuCommandService.restore(any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/sys/menu/restore?ids=1,2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }

    @Test
    void testWipe() {
        when(sysMenuCommandService.wipe(any())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/sys/menu/wipe?ids=1,2,3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0);
    }
}
