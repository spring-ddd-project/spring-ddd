package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenColumnBindViewTest {

    @Test
    @DisplayName("GenColumnBindView 应支持无参构造和 setter/getter")
    void genColumnBindView_shouldSupportGetterSetter() {
        GenColumnBindView view = new GenColumnBindView();
        view.setId(1L);
        view.setColumnType("varchar");
        view.setEntityType("String");
        view.setComponentType((byte) 1);
        view.setTypescriptType((byte) 2);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getColumnType()).isEqualTo("varchar");
        assertThat(view.getEntityType()).isEqualTo("String");
        assertThat(view.getComponentType()).isEqualTo((byte) 1);
        assertThat(view.getTypescriptType()).isEqualTo((byte) 2);
    }

    @Test
    @DisplayName("GenColumnBindView 初始值应为 null")
    void genColumnBindView_shouldHaveNullInitialValues() {
        GenColumnBindView view = new GenColumnBindView();

        assertThat(view.getId()).isNull();
        assertThat(view.getColumnType()).isNull();
        assertThat(view.getEntityType()).isNull();
        assertThat(view.getComponentType()).isNull();
        assertThat(view.getTypescriptType()).isNull();
    }
}
