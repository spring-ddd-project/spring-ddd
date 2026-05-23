package com.springddd.domain.gen;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenColumnsExtendInfoTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        GenColumnsExtendInfo obj = new GenColumnsExtendInfo(1L, (byte) 1);
        assertThat(obj.propDictId()).isEqualTo(1L);
        assertThat(obj.typescriptType()).isEqualTo((byte) 1);
    }

}