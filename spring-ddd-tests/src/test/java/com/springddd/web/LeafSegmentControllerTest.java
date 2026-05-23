package com.springddd.web;

import com.springddd.domain.leaf.LeafSegmentDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.mockito.Mockito.*;

class LeafSegmentControllerTest {

    private WebTestClient webTestClient;
    private LeafSegmentDomainService leafSegmentDomainService;

    @BeforeEach
    void setUp() {
        leafSegmentDomainService = mock(LeafSegmentDomainService.class);
        LeafSegmentController controller = new LeafSegmentController(leafSegmentDomainService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @DisplayName("POST /leaf/segment/get/{bizTag} 应返回ID")
    void getId_shouldReturnId() {
        when(leafSegmentDomainService.getId("test")).thenReturn(Mono.just(12345L));

        webTestClient.post()
                .uri("/leaf/segment/get/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data").isEqualTo(12345);
    }

    @Test
    @DisplayName("POST /leaf/segment/get/{bizTag} 异常时应返回错误")
    void getId_shouldReturnError_whenException() {
        when(leafSegmentDomainService.getId("test")).thenReturn(Mono.error(new RuntimeException("segment error")));

        webTestClient.post()
                .uri("/leaf/segment/get/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(500);
    }

    @Test
    @DisplayName("GET /leaf/segment/cache 应返回缓存状态")
    void cache_shouldReturnCacheStatus() {
        when(leafSegmentDomainService.getCacheStatus()).thenReturn(Mono.just(Map.of("test", true)));

        webTestClient.get()
                .uri("/leaf/segment/cache")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.data.test").isEqualTo(true);
    }
}
