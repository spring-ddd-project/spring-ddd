package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenColumnBindPageQueryTest {

    @Test
    @DisplayName("GenColumnBindPageQuery 应支持 setter/getter 和继承字段")
    void genColumnBindPageQuery_shouldSupportGetterSetter() {
        GenColumnBindPageQuery query = new GenColumnBindPageQuery();
        query.setId(1L);
        query.setColumnType("varchar");
        query.setPageNum(1);
        query.setPageSize(10);

        assertThat(query.getId()).isEqualTo(1L);
        assertThat(query.getColumnType()).isEqualTo("varchar");
        assertThat(query.getPageNum()).isEqualTo(1);
        assertThat(query.getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("GenColumnBindPageQuery 初始值应为 null")
    void genColumnBindPageQuery_shouldHaveNullInitialValues() {
        GenColumnBindPageQuery query = new GenColumnBindPageQuery();

        assertThat(query.getId()).isNull();
        assertThat(query.getPageNum()).isNull();
        assertThat(query.getPageSize()).isNull();
    }
}
