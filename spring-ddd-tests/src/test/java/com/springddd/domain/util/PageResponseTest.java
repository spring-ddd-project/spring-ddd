package com.springddd.domain.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageResponseTest {

    @Test
    void testDefaultConstructor() {
        PageResponse<String> response = new PageResponse<>();
        assertNull(response.getItems());
        assertEquals(0, response.getTotal());
        assertEquals(0, response.getPageNum());
        assertEquals(0, response.getPageSize());
    }

    @Test
    void testAllArgsConstructor() {
        List<String> items = Arrays.asList("a", "b", "c");
        PageResponse<String> response = new PageResponse<>(items, 100L, 2, 10);

        assertEquals(items, response.getItems());
        assertEquals(100, response.getTotal());
        assertEquals(2, response.getPageNum());
        assertEquals(10, response.getPageSize());
    }

    @Test
    void testSetters() {
        PageResponse<String> response = new PageResponse<>();
        List<String> items = Arrays.asList("x", "y");

        response.setItems(items);
        response.setTotal(50L);
        response.setPageNum(3);
        response.setPageSize(20);

        assertEquals(items, response.getItems());
        assertEquals(50, response.getTotal());
        assertEquals(3, response.getPageNum());
        assertEquals(20, response.getPageSize());
    }

    @Test
    void testEmptyItems() {
        PageResponse<String> response = new PageResponse<>(Collections.emptyList(), 0L, 1, 10);
        assertTrue(response.getItems().isEmpty());
        assertEquals(0, response.getTotal());
    }

    @Test
    void testLargeTotal() {
        PageResponse<String> response = new PageResponse<>(
                Collections.emptyList(), Long.MAX_VALUE, 1, 10
        );
        assertEquals(Long.MAX_VALUE, response.getTotal());
    }

    @Test
    void testSingleItem() {
        PageResponse<String> response = new PageResponse<>(
                Collections.singletonList("only"), 1L, 1, 1
        );
        assertEquals(1, response.getItems().size());
        assertEquals("only", response.getItems().get(0));
    }

    @Test
    void testEqualsAndHashCode() {
        PageResponse<String> response1 = new PageResponse<>(
                Arrays.asList("a", "b"), 100L, 1, 10
        );
        PageResponse<String> response2 = new PageResponse<>(
                Arrays.asList("a", "b"), 100L, 1, 10
        );

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testNotEquals() {
        PageResponse<String> response1 = new PageResponse<>(
                Arrays.asList("a", "b"), 100L, 1, 10
        );
        PageResponse<String> response2 = new PageResponse<>(
                Arrays.asList("a", "b"), 200L, 1, 10
        );

        assertNotEquals(response1, response2);
    }

    @Test
    void testToString() {
        PageResponse<String> response = new PageResponse<>(
                Arrays.asList("a", "b"), 100L, 1, 10
        );
        String str = response.toString();

        assertTrue(str.contains("items"));
        assertTrue(str.contains("100"));
        assertTrue(str.contains("1"));
        assertTrue(str.contains("10"));
    }
}
