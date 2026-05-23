package com.springddd.domain.menu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuIdTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        MenuId obj = new MenuId(1L);
        assertThat(obj.value()).isEqualTo(1L);
    }

}