package com.springddd.domain.util;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseTest {

    @Test
    void testSuccess() {
        ApiResponse resp = ApiResponse.success("data");
        assertThat(resp.getCode()).isEqualTo(0);
        assertThat(resp.getMessage()).isEqualTo("Success");
        assertThat(resp.getData()).isEqualTo("data");
    }

    @Test
    void testEmpty() {
        ApiResponse resp = ApiResponse.empty();
        assertThat(resp.getCode()).isEqualTo(0);
        assertThat(resp.getData()).isNull();
    }

    @Test
    void testErrorWithCode() {
        ApiResponse resp = ApiResponse.error(404, "Not Found");
        assertThat(resp.getCode()).isEqualTo(404);
        assertThat(resp.getMessage()).isEqualTo("Not Found");
        assertThat(resp.getData()).isNull();
    }

    @Test
    void testErrorWithMessage() {
        ApiResponse resp = ApiResponse.error("Something went wrong");
        assertThat(resp.getCode()).isEqualTo(500);
        assertThat(resp.getMessage()).isEqualTo("Something went wrong");
    }

    @Test
    void testOkMono() {
        StepVerifier.create(ApiResponse.ok(Mono.just("hello")))
                .assertNext(resp -> {
                    assertThat(resp.getCode()).isEqualTo(0);
                    assertThat(resp.getData()).isEqualTo("hello");
                })
                .verifyComplete();
    }

    @Test
    void testOkMonoEmpty() {
        StepVerifier.create(ApiResponse.ok(Mono.empty()))
                .assertNext(resp -> {
                    assertThat(resp.getCode()).isEqualTo(0);
                    assertThat(resp.getData()).isNull();
                })
                .verifyComplete();
    }

    @Test
    void testOkMonoError() {
        StepVerifier.create(ApiResponse.ok(Mono.error(new RuntimeException("fail"))))
                .assertNext(resp -> {
                    assertThat(resp.getCode()).isEqualTo(500);
                    assertThat(resp.getMessage()).isEqualTo("fail");
                })
                .verifyComplete();
    }

    @Test
    void testOkMonoWithPageResponse() {
        PageResponse<String> page = new PageResponse<>(List.of("a", "b"), 100L, 1, 10);
        StepVerifier.create(ApiResponse.ok(Mono.just(page)))
                .assertNext(resp -> {
                    assertThat(resp.getCode()).isEqualTo(0);
                    assertThat(resp.getData()).isInstanceOf(PageResponse.class);
                })
                .verifyComplete();
    }

    @Test
    void testOkFlux() {
        StepVerifier.create(ApiResponse.ok(Flux.just("a", "b")))
                .assertNext(resp -> {
                    assertThat(resp.getCode()).isEqualTo(0);
                    assertThat(resp.getData()).isEqualTo(List.of("a", "b"));
                })
                .verifyComplete();
    }

    @Test
    void testOkFluxEmpty() {
        StepVerifier.create(ApiResponse.ok(Flux.empty()))
                .assertNext(resp -> {
                    assertThat(resp.getCode()).isEqualTo(0);
                    assertThat(resp.getData()).isEqualTo(List.of());
                })
                .verifyComplete();
    }

    @Test
    void testValidated() {
        StepVerifier.create(ApiResponse.validated(Mono.just("input"), input -> Mono.just("output")))
                .assertNext(resp -> {
                    assertThat(resp.getCode()).isEqualTo(0);
                    assertThat(resp.getData()).isEqualTo("output");
                })
                .verifyComplete();
    }

    @Test
    void testValidatedEmpty() {
        StepVerifier.create(ApiResponse.validated(Mono.just("input"), input -> Mono.empty()))
                .assertNext(resp -> {
                    assertThat(resp.getCode()).isEqualTo(0);
                    assertThat(resp.getData()).isNull();
                })
                .verifyComplete();
    }

    @Test
    void testPage() {
        ApiResponse resp = ApiResponse.page(List.of("a"), 100L, 1, 10);
        assertThat(resp.getCode()).isEqualTo(0);
        assertThat(resp.getData()).isInstanceOf(PageResponse.class);
    }
}
