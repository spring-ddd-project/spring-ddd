package com.springddd.domain.util;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void testSuccessWithData() {
        ApiResponse response = ApiResponse.success("test data");
        assertEquals(0, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals("test data", response.getData());
    }

    @Test
    void testSuccessWithNullData() {
        ApiResponse response = ApiResponse.success(null);
        assertEquals(0, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testEmpty() {
        ApiResponse response = ApiResponse.empty();
        assertEquals(0, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testErrorWithCodeAndMessage() {
        ApiResponse response = ApiResponse.error(500, "Internal error");
        assertEquals(500, response.getCode());
        assertEquals("Internal error", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testErrorWithMessageOnly() {
        ApiResponse response = ApiResponse.error("Bad request");
        assertEquals(500, response.getCode());
        assertEquals("Bad request", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testOkMonoWithData() {
        Mono<String> mono = Mono.just("test data");
        Mono<ApiResponse> responseMono = ApiResponse.ok(mono);

        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertEquals("Success", response.getMessage());
                    assertEquals("test data", response.getData());
                })
                .verifyComplete();
    }

    @Test
    void testOkMonoWithPageResponse() {
        PageResponse<String> pageResponse = new PageResponse<>(
                Arrays.asList("item1", "item2"), 2, 1, 10
        );
        Mono<PageResponse<String>> mono = Mono.just(pageResponse);

        Mono<ApiResponse> responseMono = ApiResponse.ok(mono);

        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertEquals("Success", response.getMessage());
                    assertNotNull(response.getData());
                    assertTrue(response.getData() instanceof PageResponse);
                    PageResponse<?> pr = (PageResponse<?>) response.getData();
                    assertEquals(2, pr.getTotal());
                    assertEquals(1, pr.getPageNum());
                    assertEquals(10, pr.getPageSize());
                })
                .verifyComplete();
    }

    @Test
    void testOkMonoEmpty() {
        Mono<String> mono = Mono.empty();
        Mono<ApiResponse> responseMono = ApiResponse.ok(mono);

        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertEquals("Success", response.getMessage());
                    assertNull(response.getData());
                })
                .verifyComplete();
    }

    @Test
    void testValidatedWithData() {
        Mono<String> paramMono = Mono.just("test");
        Function<String, Mono<String>> handler = str -> Mono.just(str + " result");

        Mono<ApiResponse> responseMono = ApiResponse.validated(paramMono, handler);

        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertEquals("Success", response.getMessage());
                    assertEquals("test result", response.getData());
                })
                .verifyComplete();
    }

    @Test
    void testValidatedWithPageResponse() {
        Mono<PageResponse<String>> paramMono = Mono.just(
                new PageResponse<>(Arrays.asList("a", "b"), 2, 1, 10)
        );
        Function<PageResponse<String>, Mono<PageResponse<String>>> handler = Mono::just;

        Mono<ApiResponse> responseMono = ApiResponse.validated(paramMono, handler);

        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertNotNull(response.getData());
                    assertTrue(response.getData() instanceof PageResponse);
                    PageResponse<?> pr = (PageResponse<?>) response.getData();
                    assertEquals(2, pr.getTotal());
                })
                .verifyComplete();
    }

    @Test
    void testValidatedEmpty() {
        Mono<String> paramMono = Mono.empty();
        Function<String, Mono<String>> handler = str -> Mono.just(str + " result");

        Mono<ApiResponse> responseMono = ApiResponse.validated(paramMono, handler);

        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertEquals("Success", response.getMessage());
                    assertNull(response.getData());
                })
                .verifyComplete();
    }

    @Test
    void testOkFluxWithData() {
        reactor.core.publisher.Flux<String> flux = reactor.core.publisher.Flux.just("a", "b", "c");
        Mono<ApiResponse> responseMono = ApiResponse.ok(flux);

        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertEquals("Success", response.getMessage());
                    assertNotNull(response.getData());
                    @SuppressWarnings("unchecked")
                    List<String> data = (List<String>) response.getData();
                    assertEquals(3, data.size());
                    assertEquals("a", data.get(0));
                })
                .verifyComplete();
    }

    @Test
    void testOkFluxEmpty() {
        reactor.core.publisher.Flux<String> flux = reactor.core.publisher.Flux.empty();
        Mono<ApiResponse> responseMono = ApiResponse.ok(flux);

        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertEquals("Success", response.getMessage());
                    assertNull(response.getData());
                })
                .verifyComplete();
    }

    @Test
    void testPage() {
        List<String> records = Arrays.asList("item1", "item2", "item3");
        ApiResponse response = ApiResponse.page(records, 100, 2, 10);

        assertEquals(0, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertTrue(response.getData() instanceof PageResponse);
        PageResponse<?> pr = (PageResponse<?>) response.getData();
        assertEquals(3, pr.getItems().size());
        assertEquals(100, pr.getTotal());
        assertEquals(2, pr.getPageNum());
        assertEquals(10, pr.getPageSize());
    }

    @Test
    void testPageWithEmptyRecords() {
        ApiResponse response = ApiResponse.page(Collections.emptyList(), 0, 1, 10);

        assertEquals(0, response.getCode());
        assertEquals("Success", response.getMessage());
        PageResponse<?> pr = (PageResponse<?>) response.getData();
        assertTrue(pr.getItems().isEmpty());
        assertEquals(0, pr.getTotal());
    }
}
