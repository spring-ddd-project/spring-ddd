package com.springddd.domain.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageResponseTest {

    @Test
    void testConstructor() {
        PageResponse<String> page = new PageResponse<>(List.of("a", "b"), 100L, 1, 10);
        assertThat(page.getList()).containsExactly("a", "b");
        assertThat(page.getItems()).containsExactly("a", "b");
        assertThat(page.getTotal()).isEqualTo(100L);
        assertThat(page.getPageNum()).isEqualTo(1);
        assertThat(page.getPageSize()).isEqualTo(10);
    }

    @Test
    void testIterator() {
        PageResponse<String> page = new PageResponse<>(List.of("a", "b"), 100L, 1, 10);
        assertThat(page.iterator()).hasNext();
        assertThat(page.iterator().next()).isEqualTo("a");
    }

    @Test
    void testNoArgsConstructor() {
        PageResponse<String> page = new PageResponse<>();
        assertThat(page.getList()).isNull();
        assertThat(page.getTotal()).isNull();
    }
}
