package com.springddd.domain.leaf.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LeafExceptionTest {

    @Test
    void testLeafAllocKeyNotExistsException() {
        LeafAllocKeyNotExistsException ex = new LeafAllocKeyNotExistsException("test-key");
        assertThat(ex.getMessage()).isEqualTo("LeafAlloc key not exists: test-key");
    }

    @Test
    void testLeafAllocNotFoundException() {
        LeafAllocNotFoundException ex = new LeafAllocNotFoundException();
        assertThat(ex.getMessage()).isEqualTo("error.leaf.alloc.notFound");
    }
}
