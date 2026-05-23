package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenTableInfoPageQueryTest {

    @Test
    @DisplayName("GenTableInfoPageQuery 应支持 setter/getter 和继承字段")
    void genTableInfoPageQuery_shouldSupportGetterSetter() {
        GenTableInfoPageQuery query = new GenTableInfoPageQuery();
        query.setTableName("sys_user");
        query.setDatabaseName("spring_ddd");
        query.setPageNum(1);
        query.setPageSize(10);

        assertThat(query.getTableName()).isEqualTo("sys_user");
        assertThat(query.getDatabaseName()).isEqualTo("spring_ddd");
        assertThat(query.getPageNum()).isEqualTo(1);
        assertThat(query.getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("GenTableInfoPageQuery 初始值应为 null")
    void genTableInfoPageQuery_shouldHaveNullInitialValues() {
        GenTableInfoPageQuery query = new GenTableInfoPageQuery();

        assertThat(query.getTableName()).isNull();
        assertThat(query.getDatabaseName()).isNull();
        assertThat(query.getPageNum()).isNull();
        assertThat(query.getPageSize()).isNull();
    }
}
