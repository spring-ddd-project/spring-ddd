package com.springddd.web;

import com.springddd.domain.leaf.SnowflakeDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

class SnowflakeControllerTest {

    private WebTestClient webTestClient;
    private SnowflakeDomainService snowflakeDomainService;

    @BeforeEach
    void setUp() {
        snowflakeDomainService = mock(SnowflakeDomainService.class);
        SnowflakeController controller = new SnowflakeController(snowflakeDomainService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @DisplayName("POST /leaf/snowflake/get 应返回生成的 Snowflake ID")
    void getId_shouldReturnSnowflakeId() {
        when(snowflakeDomainService.getId()).thenReturn(Mono.just(1234567890123L));

        webTestClient.post()
                .uri("/leaf/snowflake/get")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data").isEqualTo(1234567890123L);
    }

    @Test
    @DisplayName("POST /leaf/snowflake/get 当服务禁用时应返回错误")
    void getId_shouldReturnError_whenDisabled() {
        when(snowflakeDomainService.getId())
                .thenReturn(Mono.error(new IllegalStateException("Snowflake service is not enabled")));

        webTestClient.post()
                .uri("/leaf/snowflake/get")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").exists();
    }

    @Test
    @DisplayName("POST /leaf/snowflake/decode 应返回解析后的 ID 信息")
    void decodeId_shouldReturnDecodedInfo() {
        Map<String, Object> decoded = new LinkedHashMap<>();
        decoded.put("timestamp", "1577836800000(2020-01-01 00:00:00.000)");
        decoded.put("datacenterId", "1");
        decoded.put("workerId", "2");
        decoded.put("sequenceId", "3");

        when(snowflakeDomainService.decodeId(12345L)).thenReturn(Mono.just(decoded));

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/leaf/snowflake/decode")
                        .queryParam("snowflakeId", 12345L)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.timestamp").isEqualTo("1577836800000(2020-01-01 00:00:00.000)")
                .jsonPath("$.data.datacenterId").isEqualTo("1")
                .jsonPath("$.data.workerId").isEqualTo("2")
                .jsonPath("$.data.sequenceId").isEqualTo("3");
    }
}
