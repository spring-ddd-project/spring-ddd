package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenColumnBindQueryTest {

    @Test
    @DisplayName("GenColumnBindQuery 应支持无参构造和 setter/getter")
    void genColumnBindQuery_shouldSupportGetterSetter() {
        GenColumnBindQuery query = new GenColumnBindQuery();
        query.setId(1L);
        query.setColumnType("varchar");
        query.setEntityType("String");
        query.setComponentType((byte) 1);
        query.setTypescriptType((byte) 2);
        query.setDeleteStatus(false);

        assertThat(query.getId()).isEqualTo(1L);
        assertThat(query.getColumnType()).isEqualTo("varchar");
        assertThat(query.getEntityType()).isEqualTo("String");
        assertThat(query.getComponentType()).isEqualTo((byte) 1);
        assertThat(query.getTypescriptType()).isEqualTo((byte) 2);
        assertThat(query.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("GenColumnBindQuery 初始值应为 null")
    void genColumnBindQuery_shouldHaveNullInitialValues() {
        GenColumnBindQuery query = new GenColumnBindQuery();

        assertThat(query.getId()).isNull();
        assertThat(query.getColumnType()).isNull();
        assertThat(query.getEntityType()).isNull();
        assertThat(query.getComponentType()).isNull();
        assertThat(query.getTypescriptType()).isNull();
        assertThat(query.getDeleteStatus()).isNull();
    }
}
