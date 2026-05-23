package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenColumnsQueryTest {

    @Test
    @DisplayName("GenColumnsQuery 应支持无参构造和 setter/getter")
    void genColumnsQuery_shouldSupportGetterSetter() {
        GenColumnsQuery query = new GenColumnsQuery();
        query.setId(1L);
        query.setInfoId(2L);
        query.setPropColumnKey("username");
        query.setPropColumnName("用户名");
        query.setDeleteStatus(false);

        assertThat(query.getId()).isEqualTo(1L);
        assertThat(query.getInfoId()).isEqualTo(2L);
        assertThat(query.getPropColumnKey()).isEqualTo("username");
        assertThat(query.getPropColumnName()).isEqualTo("用户名");
        assertThat(query.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("GenColumnsQuery 初始值应为 null")
    void genColumnsQuery_shouldHaveNullInitialValues() {
        GenColumnsQuery query = new GenColumnsQuery();

        assertThat(query.getId()).isNull();
        assertThat(query.getInfoId()).isNull();
        assertThat(query.getPropColumnKey()).isNull();
        assertThat(query.getDeleteStatus()).isNull();
    }
}
