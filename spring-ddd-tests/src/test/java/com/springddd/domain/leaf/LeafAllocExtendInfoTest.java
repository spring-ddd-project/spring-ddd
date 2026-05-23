package com.springddd.domain.leaf;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LeafAllocExtendInfoTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        LeafAllocExtendInfo obj = new LeafAllocExtendInfo(1L, 1);
        assertThat(obj.maxId()).isEqualTo(1L);
        assertThat(obj.step()).isEqualTo(1);
    }

    @Test
    @DisplayName("step 为 0 应抛异常")
    void constructor_withZeroStep_shouldThrowException() {
        assertThatThrownBy(() -> {
            int step = 0;
            new LeafAllocExtendInfo(1L, step);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("step 为负数应抛异常")
    void constructor_withNegativeStep_shouldThrowException() {
        assertThatThrownBy(() -> {
            int step = -1;
            new LeafAllocExtendInfo(1L, step);
        }).isInstanceOf(IllegalArgumentException.class);
    }

}