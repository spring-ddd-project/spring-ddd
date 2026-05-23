package com.springddd.domain.dict;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DictItemIdTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        DictItemId obj = new DictItemId(1L);
        assertThat(obj.value()).isEqualTo(1L);
    }

}