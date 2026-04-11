package com.springddd.domain.util;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseReactiveTest {

    @Test
    void okMono_shouldReturnSuccessWithData() {
        Mono<String> mono = Mono.just("test data");

        StepVerifier.create(ApiResponse.ok(mono))
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertEquals("Success", response.getMessage());
                    assertEquals("test data", response.getData());
                })
                .verifyComplete();
    }

    @Test
    void okMono_shouldReturnEmptyWhenNoData() {
        Mono<String> mono = Mono.empty();

        StepVerifier.create(ApiResponse.ok(mono))
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertEquals("Success", response.getMessage());
                    assertNull(response.getData());
                })
                .verifyComplete();
    }

    @Test
    void okMono_shouldHandlePageResponse() {
        List<String> items = Arrays.asList("a", "b", "c");
        PageResponse<String> pageResponse = new PageResponse<>(items, 10L, 1, 3);
        Mono<PageResponse<String>> mono = Mono.just(pageResponse);

        StepVerifier.create(ApiResponse.ok(mono))
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertEquals("Success", response.getMessage());
                    PageResponse<?> data = (PageResponse<?>) response.getData();
                    assertEquals(3, data.getItems().size());
                    assertEquals(10L, data.getTotal());
                })
                .verifyComplete();
    }

    @Test
    void okFlux_shouldReturnSuccessWithList() {
        Flux<String> flux = Flux.just("a", "b", "c");

        StepVerifier.create(ApiResponse.ok(flux))
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertEquals("Success", response.getMessage());
                    @SuppressWarnings("unchecked")
                    List<String> data = (List<String>) response.getData();
                    assertEquals(3, data.size());
                })
                .verifyComplete();
    }

    @Test
    void okFlux_shouldReturnEmptyWhenNoData() {
        Flux<String> flux = Flux.empty();

        StepVerifier.create(ApiResponse.ok(flux))
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertEquals("Success", response.getMessage());
                    @SuppressWarnings("unchecked")
                    List<String> data = (List<String>) response.getData();
                    assertTrue(data.isEmpty());
                })
                .verifyComplete();
    }

    @Test
    void validated_shouldApplyHandlerAndReturnSuccess() {
        Mono<String> paramMono = Mono.just("input");
        Function<String, Mono<String>> handler = input -> Mono.just("processed: " + input);

        StepVerifier.create(ApiResponse.validated(paramMono, handler))
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertEquals("Success", response.getMessage());
                    assertEquals("processed: input", response.getData());
                })
                .verifyComplete();
    }

    @Test
    void validated_shouldReturnEmptyWhenNoResult() {
        Mono<String> paramMono = Mono.just("input");
        Function<String, Mono<String>> handler = input -> Mono.empty();

        StepVerifier.create(ApiResponse.validated(paramMono, handler))
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertEquals("Success", response.getMessage());
                    assertNull(response.getData());
                })
                .verifyComplete();
    }

    @Test
    void page_shouldReturnSuccessWithPageResponse() {
        List<String> items = Arrays.asList("a", "b", "c");

        ApiResponse response = ApiResponse.page(items, 10L, 1, 3);

        assertEquals(0, response.getCode());
        assertEquals("Success", response.getMessage());
        PageResponse<?> data = (PageResponse<?>) response.getData();
        assertEquals(3, data.getItems().size());
        assertEquals(10L, data.getTotal());
        assertEquals(1, data.getPageNum());
        assertEquals(3, data.getPageSize());
    }
}
