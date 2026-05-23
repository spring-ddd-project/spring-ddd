package com.springddd.domain.gen;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ColumnBindIdTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        ColumnBindId obj = new ColumnBindId(1L);
        assertThat(obj.value()).isEqualTo(1L);
    }

}