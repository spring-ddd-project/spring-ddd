package com.springddd.domain.dept;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeptIdTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        DeptId obj = new DeptId(1L);
        assertThat(obj.value()).isEqualTo(1L);
    }

}