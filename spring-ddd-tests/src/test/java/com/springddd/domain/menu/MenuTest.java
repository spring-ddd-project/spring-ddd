package com.springddd.domain.menu;

import com.springddd.domain.menu.exception.MenuComponentNullException;
import com.springddd.domain.menu.exception.MenuPathNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        Menu menu = new Menu("/sys/user", "system/user/index", true, false, false);
        assertThat(menu.menuPath()).isEqualTo("/sys/user");
        assertThat(menu.component()).isEqualTo("system/user/index");
        assertThat(menu.affixTab()).isTrue();
        assertThat(menu.noBasicLayout()).isFalse();
        assertThat(menu.embedded()).isFalse();
    }

    @Test
    @DisplayName("menuPath 为 null 应抛 MenuPathNullException")
    void constructor_withNullMenuPath_shouldThrowException() {
        assertThatThrownBy(() -> new Menu(null, "system/user/index", true, false, false))
                .isInstanceOf(MenuPathNullException.class);
    }

    @Test
    @DisplayName("menuPath 为空字符串应抛 MenuPathNullException")
    void constructor_withEmptyMenuPath_shouldThrowException() {
        assertThatThrownBy(() -> new Menu("", "system/user/index", true, false, false))
                .isInstanceOf(MenuPathNullException.class);
    }

    @Test
    @DisplayName("component 为 null 应抛 MenuComponentNullException")
    void constructor_withNullComponent_shouldThrowException() {
        assertThatThrownBy(() -> new Menu("/sys/user", null, true, false, false))
                .isInstanceOf(MenuComponentNullException.class);
    }

    @Test
    @DisplayName("component 为空字符串应抛 MenuComponentNullException")
    void constructor_withEmptyComponent_shouldThrowException() {
        assertThatThrownBy(() -> new Menu("/sys/user", "", true, false, false))
                .isInstanceOf(MenuComponentNullException.class);
    }

    @Test
    @DisplayName("可选字段可为 null")
    void constructor_withNullOptionalFields_shouldCreate() {
        Menu menu = new Menu("/sys/user", "system/user/index", null, null, null);
        assertThat(menu.affixTab()).isNull();
        assertThat(menu.noBasicLayout()).isNull();
        assertThat(menu.embedded()).isNull();
    }
}
