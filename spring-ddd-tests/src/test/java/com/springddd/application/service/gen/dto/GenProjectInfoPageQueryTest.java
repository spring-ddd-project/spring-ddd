package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenProjectInfoPageQueryTest {

    @Test
    @DisplayName("GenProjectInfoPageQuery 应支持 setter/getter 和继承字段")
    void genProjectInfoPageQuery_shouldSupportGetterSetter() {
        GenProjectInfoPageQuery query = new GenProjectInfoPageQuery();
        query.setId(1L);
        query.setTableName("sys_user");
        query.setPageNum(1);
        query.setPageSize(10);

        assertThat(query.getId()).isEqualTo(1L);
        assertThat(query.getTableName()).isEqualTo("sys_user");
        assertThat(query.getPageNum()).isEqualTo(1);
        assertThat(query.getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("GenProjectInfoPageQuery 初始值应为 null")
    void genProjectInfoPageQuery_shouldHaveNullInitialValues() {
        GenProjectInfoPageQuery query = new GenProjectInfoPageQuery();

        assertThat(query.getId()).isNull();
        assertThat(query.getPageNum()).isNull();
        assertThat(query.getPageSize()).isNull();
    }
}
