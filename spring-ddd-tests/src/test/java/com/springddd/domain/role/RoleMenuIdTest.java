package com.springddd.domain.role;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleMenuIdTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        RoleMenuId obj = new RoleMenuId(1L);
        assertThat(obj.value()).isEqualTo(1L);
    }

}