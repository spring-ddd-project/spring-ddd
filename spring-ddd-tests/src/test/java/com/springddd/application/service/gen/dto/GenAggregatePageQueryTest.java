package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenAggregatePageQueryTest {

    @Test
    @DisplayName("GenAggregatePageQuery 应支持 setter/getter 和继承字段")
    void genAggregatePageQuery_shouldSupportGetterSetter() {
        GenAggregatePageQuery query = new GenAggregatePageQuery();
        query.setId(1L);
        query.setInfoId(2L);
        query.setObjectName("SysUser");
        query.setPageNum(1);
        query.setPageSize(10);

        assertThat(query.getId()).isEqualTo(1L);
        assertThat(query.getInfoId()).isEqualTo(2L);
        assertThat(query.getObjectName()).isEqualTo("SysUser");
        assertThat(query.getPageNum()).isEqualTo(1);
        assertThat(query.getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("GenAggregatePageQuery 初始值应为 null")
    void genAggregatePageQuery_shouldHaveNullInitialValues() {
        GenAggregatePageQuery query = new GenAggregatePageQuery();

        assertThat(query.getId()).isNull();
        assertThat(query.getPageNum()).isNull();
        assertThat(query.getPageSize()).isNull();
    }
}
