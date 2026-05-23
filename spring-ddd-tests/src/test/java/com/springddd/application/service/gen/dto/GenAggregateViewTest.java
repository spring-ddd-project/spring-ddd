package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenAggregateViewTest {

    @Test
    @DisplayName("GenAggregateView 应支持无参构造和 setter/getter")
    void genAggregateView_shouldSupportGetterSetter() {
        GenAggregateView view = new GenAggregateView();
        view.setId(1L);
        view.setInfoId(2L);
        view.setObjectName("SysUser");
        view.setObjectValue("user");
        view.setObjectType((byte) 1);
        view.setHasCreated(true);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getInfoId()).isEqualTo(2L);
        assertThat(view.getObjectName()).isEqualTo("SysUser");
        assertThat(view.getObjectValue()).isEqualTo("user");
        assertThat(view.getObjectType()).isEqualTo((byte) 1);
        assertThat(view.getHasCreated()).isTrue();
    }

    @Test
    @DisplayName("GenAggregateView 初始值应为 null")
    void genAggregateView_shouldHaveNullInitialValues() {
        GenAggregateView view = new GenAggregateView();

        assertThat(view.getId()).isNull();
        assertThat(view.getInfoId()).isNull();
        assertThat(view.getObjectName()).isNull();
        assertThat(view.getObjectValue()).isNull();
        assertThat(view.getObjectType()).isNull();
        assertThat(view.getHasCreated()).isNull();
    }
}
