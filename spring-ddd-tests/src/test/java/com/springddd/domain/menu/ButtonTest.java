package com.springddd.domain.menu;

import com.springddd.domain.menu.exception.MenuPermissionNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ButtonTest {

    @Test
    @DisplayName("当权限不为空时应成功创建 Button")
    void shouldCreateButton_whenPermissionIsNotEmpty() {
        Button button = new Button("sys:user:add", "/api/user");
        assertThat(button.permission()).isEqualTo("sys:user:add");
        assertThat(button.api()).isEqualTo("/api/user");
    }

    @Test
    @DisplayName("当 api 为 null 时应成功创建 Button")
    void shouldCreateButton_whenApiIsNull() {
        Button button = new Button("sys:user:add");
        assertThat(button.permission()).isEqualTo("sys:user:add");
        assertThat(button.api()).isNull();
    }

    @Test
    @DisplayName("当权限为空时应抛出 MenuPermissionNullException")
    void shouldThrowException_whenPermissionIsEmpty() {
        assertThatThrownBy(() -> new Button(""))
                .isInstanceOf(MenuPermissionNullException.class);
    }

    @Test
    @DisplayName("当权限为 null 时应抛出 MenuPermissionNullException")
    void shouldThrowException_whenPermissionIsNull() {
        assertThatThrownBy(() -> new Button(null))
                .isInstanceOf(MenuPermissionNullException.class);
    }
}
