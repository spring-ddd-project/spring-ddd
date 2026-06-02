package com.springddd.domain.util;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void shouldCreateSuccessResponse() {
        ApiResponse response = ApiResponse.success("data");
        assertEquals(0, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals("data", response.getData());
    }

    @Test
    void shouldCreateEmptyResponse() {
        ApiResponse response = ApiResponse.empty();
        assertEquals(0, response.getCode());
        assertNull(response.getData());
    }

    @Test
    void shouldCreateErrorResponse() {
        ApiResponse response = ApiResponse.error("Error occurred");
        assertEquals(500, response.getCode());
        assertEquals("Error occurred", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void shouldCreateErrorResponseWithCode() {
        ApiResponse response = ApiResponse.error(404, "Not found");
        assertEquals(404, response.getCode());
        assertEquals("Not found", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        ApiResponse response = ApiResponse.success("data");
        String str = response.toString();
        assertTrue(str.contains("ApiResponse"));
    }

    @Test
    void constructor_shouldSetAllFields() {
        ApiResponse response = new ApiResponse(200, "OK", "payload");
        assertEquals(200, response.getCode());
        assertEquals("OK", response.getMessage());
        assertEquals("payload", response.getData());
    }

    @Test
    void validated_shouldReturnPageResponse_whenHandlerReturnsPage() {
        PageResponse<String> page = new PageResponse<>(List.of("a", "b"), 2, 1, 10);
        reactor.test.StepVerifier.create(ApiResponse.validated(reactor.core.publisher.Mono.just("param"), p -> reactor.core.publisher.Mono.just(page)))
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertNotNull(response.getData());
                })
                .verifyComplete();
    }
}
