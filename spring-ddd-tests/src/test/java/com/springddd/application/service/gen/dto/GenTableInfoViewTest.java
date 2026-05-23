package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class GenTableInfoViewTest {

    @Test
    @DisplayName("GenTableInfoView 应支持无参构造和 setter/getter")
    void genTableInfoView_shouldSupportGetterSetter() {
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0, 0);

        GenTableInfoView view = new GenTableInfoView();
        view.setTableSchema("spring_ddd");
        view.setTableName("sys_user");
        view.setTableComment("用户表");
        view.setCreateTime(now);
        view.setTableCollation("utf8mb4_unicode_ci");

        assertThat(view.getTableSchema()).isEqualTo("spring_ddd");
        assertThat(view.getTableName()).isEqualTo("sys_user");
        assertThat(view.getTableComment()).isEqualTo("用户表");
        assertThat(view.getCreateTime()).isEqualTo(now);
        assertThat(view.getTableCollation()).isEqualTo("utf8mb4_unicode_ci");
    }

    @Test
    @DisplayName("GenTableInfoView 应支持全参构造")
    void genTableInfoView_shouldSupportAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0, 0);

        GenTableInfoView view = new GenTableInfoView("spring_ddd", "sys_user", "用户表", now, "utf8mb4_unicode_ci");

        assertThat(view.getTableSchema()).isEqualTo("spring_ddd");
        assertThat(view.getTableName()).isEqualTo("sys_user");
        assertThat(view.getTableComment()).isEqualTo("用户表");
        assertThat(view.getCreateTime()).isEqualTo(now);
        assertThat(view.getTableCollation()).isEqualTo("utf8mb4_unicode_ci");
    }

    @Test
    @DisplayName("GenTableInfoView 初始值应为 null")
    void genTableInfoView_shouldHaveNullInitialValues() {
        GenTableInfoView view = new GenTableInfoView();

        assertThat(view.getTableSchema()).isNull();
        assertThat(view.getTableName()).isNull();
        assertThat(view.getTableComment()).isNull();
        assertThat(view.getCreateTime()).isNull();
        assertThat(view.getTableCollation()).isNull();
    }
}
