package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenColumnsViewTest {

    @Test
    @DisplayName("GenColumnsView 应支持无参构造和 setter")
    void genColumnsView_shouldSupportNoArgConstructor() {
        GenColumnsView view = new GenColumnsView();
        view.setId(1L);
        view.setPropColumnKey("username");
        view.setPropColumnName("用户名");
        view.setPropColumnType("varchar");
        view.setPropColumnComment("用户名称");
        view.setFormVisible(true);
        view.setFormRequired(true);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getPropColumnKey()).isEqualTo("username");
        assertThat(view.getPropColumnName()).isEqualTo("用户名");
        assertThat(view.getPropColumnType()).isEqualTo("varchar");
        assertThat(view.getPropColumnComment()).isEqualTo("用户名称");
        assertThat(view.getFormVisible()).isTrue();
        assertThat(view.getFormRequired()).isTrue();
    }

    @Test
    @DisplayName("GenColumnsView 应支持全参构造")
    void genColumnsView_shouldSupportAllArgsConstructor() {
        GenColumnsView view = new GenColumnsView(1L, 2L, "username", "用户名", "varchar", "用户名称",
                "String", "String", 1L, "dict", true, true, true, (byte) 1, (byte) 2, (byte) 3,
                (byte) 4, "filterComp", "filterType", "tsType", "formComp", true, true, "en", "locale");

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getInfoId()).isEqualTo(2L);
        assertThat(view.getPropColumnKey()).isEqualTo("username");
        assertThat(view.getPropColumnName()).isEqualTo("用户名");
        assertThat(view.getEn()).isEqualTo("en");
        assertThat(view.getLocale()).isEqualTo("locale");
    }

    @Test
    @DisplayName("GenColumnsView 应支持便捷构造")
    void genColumnsView_shouldSupportConvenienceConstructor() {
        GenColumnsView view = new GenColumnsView("username", "用户名", "varchar", "用户名称");

        assertThat(view.getPropColumnKey()).isEqualTo("username");
        assertThat(view.getPropColumnName()).isEqualTo("用户名");
        assertThat(view.getPropColumnType()).isEqualTo("varchar");
        assertThat(view.getPropColumnComment()).isEqualTo("用户名称");
    }
}
