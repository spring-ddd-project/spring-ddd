package com.springddd.domain.menu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AdvancedOptionsTest {

    @Test
    @DisplayName("默认构造应正确赋值")
    void constructor_withDefault_shouldCreate() {
        AdvancedOptions options = new AdvancedOptions(1, "icon", 1, true, true);
        assertThat(options.order()).isEqualTo(1);
        assertThat(options.icon()).isEqualTo("icon");
        assertThat(options.menuType()).isEqualTo(1);
        assertThat(options.visible()).isTrue();
        assertThat(options.menuStatus()).isTrue();
    }

    @Test
    @DisplayName("Catalog 构造应正确赋值")
    void constructor_withCatalog_shouldCreate() {
        AdvancedOptions options = new AdvancedOptions(1, 1, "icon", true, true);
        assertThat(options.order()).isEqualTo(1);
        assertThat(options.icon()).isEqualTo("icon");
        assertThat(options.menuType()).isEqualTo(1);
        assertThat(options.visible()).isTrue();
        assertThat(options.menuStatus()).isTrue();
    }

    @Test
    @DisplayName("Button 构造应正确赋值")
    void constructor_withButton_shouldCreate() {
        AdvancedOptions options = new AdvancedOptions(1, 2, true);
        assertThat(options.order()).isEqualTo(1);
        assertThat(options.icon()).isNull();
        assertThat(options.menuType()).isEqualTo(2);
        assertThat(options.visible()).isNull();
        assertThat(options.menuStatus()).isTrue();
    }

    @Test
    @DisplayName("Button 构造 visible 可为 false")
    void constructor_withButtonAndFalseStatus_shouldCreate() {
        AdvancedOptions options = new AdvancedOptions(1, 2, false);
        assertThat(options.order()).isEqualTo(1);
        assertThat(options.icon()).isNull();
        assertThat(options.menuType()).isEqualTo(2);
        assertThat(options.visible()).isNull();
        assertThat(options.menuStatus()).isFalse();
    }
}
