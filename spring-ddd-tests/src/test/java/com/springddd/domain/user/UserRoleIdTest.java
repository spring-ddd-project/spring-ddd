package com.springddd.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserRoleIdTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        UserRoleId obj = new UserRoleId(1L);
        assertThat(obj.value()).isEqualTo(1L);
    }

}