package com.springddd.domain.role;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ColumnPermissionTest {

    @Test
    @DisplayName("record 应正确存储字段")
    void record_shouldStoreFields() {
        ColumnPermission perm = new ColumnPermission("sys_user", "用户", List.of("id", "name"));
        assertThat(perm.entityCode()).isEqualTo("sys_user");
        assertThat(perm.entityName()).isEqualTo("用户");
        assertThat(perm.columns()).containsExactly("id", "name");
    }
}
