package com.springddd.domain.leaf;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LeafAllocIdTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        LeafAllocId obj = new LeafAllocId("test");
        assertThat(obj.value()).isEqualTo("test");
    }

}