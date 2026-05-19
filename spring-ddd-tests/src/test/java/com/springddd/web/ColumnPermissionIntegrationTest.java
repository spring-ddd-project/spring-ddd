package com.springddd.web;

import com.springddd.application.service.permission.ColumnPermissionMetadataService;
import com.springddd.domain.permission.EntityColumnMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ColumnPermissionIntegrationTest {

    private WebTestClient webTestClient;
    private ColumnPermissionMetadataService metadataService;

    @BeforeEach
    void setUp() {
        metadataService = mock(ColumnPermissionMetadataService.class);
        ColumnPermissionMetadataController controller = new ColumnPermissionMetadataController(metadataService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @DisplayName("POST /sys/column-permission/entities 应返回实体元数据列表")
    void getAllEntities_shouldReturnMetadataList() {
        List<EntityColumnMetadata> metadata = List.of(
                new EntityColumnMetadata("sys_user", "用户管理", List.of(
                        new EntityColumnMetadata.ColumnInfo("id", "ID"),
                        new EntityColumnMetadata.ColumnInfo("username", "用户名")
                ))
        );
        when(metadataService.getAllEntityMetadata()).thenReturn(Mono.just(metadata));

        webTestClient.post()
                .uri("/sys/column-permission/entities")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data").isArray()
                .jsonPath("$.data[0].entityCode").isEqualTo("sys_user")
                .jsonPath("$.data[0].entityName").isEqualTo("用户管理");
    }

    @Test
    @DisplayName("POST /sys/column-permission/metadata 应返回当前用户的列权限配置")
    void getMetadata_shouldReturnColumnPermissions() {
        Map<String, List<String>> permissions = Map.of(
                "sys_user", List.of("username", "phone")
        );
        when(metadataService.getCurrentUserColumnPermissions())
                .thenReturn(Mono.just(permissions));

        webTestClient.post()
                .uri("/sys/column-permission/metadata")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data.sys_user").isArray()
                .jsonPath("$.data.sys_user[0]").isEqualTo("username");
    }

    @Test
    @DisplayName("POST /sys/column-permission/visible-columns 应返回指定实体的可见列")
    void getVisibleColumns_shouldReturnColumns() {
        when(metadataService.getVisibleColumns("sys_user"))
                .thenReturn(Mono.just(List.of("username", "phone")));

        webTestClient.post()
                .uri("/sys/column-permission/visible-columns?entityCode=sys_user")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data[0]").isEqualTo("username")
                .jsonPath("$.data[1]").isEqualTo("phone");
    }
}
