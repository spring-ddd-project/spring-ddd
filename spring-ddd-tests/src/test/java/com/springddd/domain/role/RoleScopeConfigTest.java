package com.springddd.domain.role;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoleScopeConfigTest {

    @Test
    @DisplayName("record 应正确存储字段")
    void record_shouldStoreFields() {
        RoleScopeConfig config = new RoleScopeConfig(List.of(1L), List.of(2L), List.of(3L), true);
        assertThat(config.depts()).containsExactly(1L);
        assertThat(config.posts()).containsExactly(2L);
        assertThat(config.users()).containsExactly(3L);
        assertThat(config.self()).isTrue();
    }
}
