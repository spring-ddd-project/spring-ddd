package com.springddd.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExtendInfoTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        ExtendInfo obj = new ExtendInfo("test", true);
        assertThat(obj.avatar()).isEqualTo("test");
        assertThat(obj.sex()).isEqualTo(true);
    }

}