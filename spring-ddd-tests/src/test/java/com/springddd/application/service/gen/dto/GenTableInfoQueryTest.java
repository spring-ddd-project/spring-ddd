package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class GenTableInfoQueryTest {

    @Test
    @DisplayName("GenTableInfoQuery 应支持无参构造和 setter/getter")
    void genTableInfoQuery_shouldSupportGetterSetter() {
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0, 0);

        GenTableInfoQuery query = new GenTableInfoQuery();
        query.setTableName("sys_user");
        query.setTableComment("用户表");
        query.setCreateTime(now);
        query.setTableCollation("utf8mb4_unicode_ci");

        assertThat(query.getTableName()).isEqualTo("sys_user");
        assertThat(query.getTableComment()).isEqualTo("用户表");
        assertThat(query.getCreateTime()).isEqualTo(now);
        assertThat(query.getTableCollation()).isEqualTo("utf8mb4_unicode_ci");
    }

    @Test
    @DisplayName("GenTableInfoQuery 初始值应为 null")
    void genTableInfoQuery_shouldHaveNullInitialValues() {
        GenTableInfoQuery query = new GenTableInfoQuery();

        assertThat(query.getTableName()).isNull();
        assertThat(query.getTableComment()).isNull();
        assertThat(query.getCreateTime()).isNull();
        assertThat(query.getTableCollation()).isNull();
    }
}
