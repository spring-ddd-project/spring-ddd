package com.springddd.domain.role;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleIdTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        RoleId obj = new RoleId(1L);
        assertThat(obj.value()).isEqualTo(1L);
    }

}