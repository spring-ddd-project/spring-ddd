package com.springddd.domain.util;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PageResponseTest {

    @Test
    void shouldCreatePageResponse() {
        List<String> data = Arrays.asList("a", "b", "c");
        PageResponse<String> response = new PageResponse<>(data, 100L, 10, 20);
        assertEquals(3, response.getItems().size());
        assertEquals(100L, response.getTotal());
        assertEquals(10, response.getPageNum());
        assertEquals(20, response.getPageSize());
    }

    @Test
    void shouldCreatePageResponseWithDefaults() {
        List<String> data = Arrays.asList("a", "b");
        PageResponse<String> response = new PageResponse<>(data, 50L, 1, 10);
        assertEquals(2, response.getItems().size());
        assertEquals(50L, response.getTotal());
    }

    @Test
    void shouldSetAndGetValues() {
        PageResponse<String> response = new PageResponse<>();
        response.setItems(Arrays.asList("a", "b"));
        response.setTotal(50L);
        response.setPageNum(1);
        response.setPageSize(10);

        assertEquals(2, response.getItems().size());
        assertEquals(50L, response.getTotal());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        List<String> data = Arrays.asList("a", "b");
        PageResponse<String> r1 = new PageResponse<>(data, 50L, 1, 10);
        PageResponse<String> r2 = new PageResponse<>(data, 50L, 1, 10);
        assertEquals(r1, r2);
    }

    @Test
    void toString_shouldReturnValueAsString() {
        List<String> data = Arrays.asList("a", "b");
        PageResponse<String> response = new PageResponse<>(data, 50L, 1, 10);
        String str = response.toString();
        assertTrue(str.contains("PageResponse"));
    }
}
