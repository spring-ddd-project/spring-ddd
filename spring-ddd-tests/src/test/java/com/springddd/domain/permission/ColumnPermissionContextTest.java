package com.springddd.domain.permission;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ColumnPermissionContextTest {

    @Test
    @DisplayName("isVisible 当 visibleColumns 为空时应返回 true")
    void isVisible_whenEmpty_shouldReturnTrue() {
        ColumnPermissionContext ctx = new ColumnPermissionContext("user", Set.of(), MaskStrategy.MASK);
        assertThat(ctx.isVisible("any")).isTrue();
    }

    @Test
    @DisplayName("isVisible 当 visibleColumns 为 null 时应返回 true")
    void isVisible_whenNull_shouldReturnTrue() {
        ColumnPermissionContext ctx = new ColumnPermissionContext("user", null, MaskStrategy.MASK);
        assertThat(ctx.isVisible("any")).isTrue();
    }

    @Test
    @DisplayName("isVisible 当列在可见列表中时应返回 true")
    void isVisible_whenInList_shouldReturnTrue() {
        ColumnPermissionContext ctx = new ColumnPermissionContext("user", Set.of("id", "name"), MaskStrategy.MASK);
        assertThat(ctx.isVisible("id")).isTrue();
    }

    @Test
    @DisplayName("isVisible 当列不在可见列表中时应返回 false")
    void isVisible_whenNotInList_shouldReturnFalse() {
        ColumnPermissionContext ctx = new ColumnPermissionContext("user", Set.of("id"), MaskStrategy.MASK);
        assertThat(ctx.isVisible("secret")).isFalse();
    }
}
