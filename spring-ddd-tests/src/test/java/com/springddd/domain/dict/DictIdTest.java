package com.springddd.domain.dict;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DictIdTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        DictId obj = new DictId(1L);
        assertThat(obj.value()).isEqualTo(1L);
    }

}