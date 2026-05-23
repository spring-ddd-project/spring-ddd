package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenAggregateQueryTest {

    @Test
    @DisplayName("GenAggregateQuery 应支持无参构造和 setter/getter")
    void genAggregateQuery_shouldSupportGetterSetter() {
        GenAggregateQuery query = new GenAggregateQuery();
        query.setId(1L);
        query.setInfoId(2L);
        query.setObjectName("SysUser");
        query.setObjectValue("user");
        query.setObjectType((byte) 1);
        query.setHasCreated(true);

        assertThat(query.getId()).isEqualTo(1L);
        assertThat(query.getInfoId()).isEqualTo(2L);
        assertThat(query.getObjectName()).isEqualTo("SysUser");
        assertThat(query.getObjectValue()).isEqualTo("user");
        assertThat(query.getObjectType()).isEqualTo((byte) 1);
        assertThat(query.getHasCreated()).isTrue();
    }

    @Test
    @DisplayName("GenAggregateQuery 初始值应为 null")
    void genAggregateQuery_shouldHaveNullInitialValues() {
        GenAggregateQuery query = new GenAggregateQuery();

        assertThat(query.getId()).isNull();
        assertThat(query.getInfoId()).isNull();
        assertThat(query.getObjectName()).isNull();
        assertThat(query.getObjectValue()).isNull();
        assertThat(query.getObjectType()).isNull();
        assertThat(query.getHasCreated()).isNull();
    }
}
