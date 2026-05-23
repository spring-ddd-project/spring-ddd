package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenTemplateQueryTest {

    @Test
    @DisplayName("GenTemplateQuery 应支持无参构造和 setter/getter")
    void genTemplateQuery_shouldSupportGetterSetter() {
        GenTemplateQuery query = new GenTemplateQuery();
        query.setId(1L);
        query.setTemplateName("index.vue");
        query.setTemplateContent("<template></template>");
        query.setDeleteStatus(false);

        assertThat(query.getId()).isEqualTo(1L);
        assertThat(query.getTemplateName()).isEqualTo("index.vue");
        assertThat(query.getTemplateContent()).isEqualTo("<template></template>");
        assertThat(query.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("GenTemplateQuery 初始值应为 null")
    void genTemplateQuery_shouldHaveNullInitialValues() {
        GenTemplateQuery query = new GenTemplateQuery();

        assertThat(query.getId()).isNull();
        assertThat(query.getTemplateName()).isNull();
        assertThat(query.getTemplateContent()).isNull();
        assertThat(query.getDeleteStatus()).isNull();
    }
}
