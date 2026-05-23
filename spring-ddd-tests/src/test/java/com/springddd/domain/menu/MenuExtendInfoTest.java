package com.springddd.domain.menu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuExtendInfoTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        MenuExtendInfo obj = new MenuExtendInfo(1, "test", "test", 1, true, true);
        assertThat(obj.order()).isEqualTo(1);
        assertThat(obj.title()).isEqualTo("test");
        assertThat(obj.icon()).isEqualTo("test");
        assertThat(obj.menuType()).isEqualTo(1);
        assertThat(obj.visible()).isEqualTo(true);
        assertThat(obj.menuStatus()).isEqualTo(true);
    }

    @Test
    @DisplayName("Button 便捷构造器")
    void buttonConstructor_shouldCreate() {
        MenuExtendInfo obj = new MenuExtendInfo(1, 1, true);
        assertThat(obj.order()).isEqualTo(1);
        assertThat(obj.menuType()).isEqualTo(1);
        assertThat(obj.menuStatus()).isEqualTo(true);
    }

    @Test
    @DisplayName("creatorId 便捷构造器")
    void creatorIdConstructor_shouldCreate() {
        MenuExtendInfo obj = new MenuExtendInfo(1L);
        assertThat(obj).isNotNull();
    }

}