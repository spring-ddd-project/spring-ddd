package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenTemplatePageQueryTest {

    @Test
    @DisplayName("GenTemplatePageQuery 应支持 setter/getter 和继承字段")
    void genTemplatePageQuery_shouldSupportGetterSetter() {
        GenTemplatePageQuery query = new GenTemplatePageQuery();
        query.setId(1L);
        query.setTemplateName("index.vue");
        query.setPageNum(1);
        query.setPageSize(10);

        assertThat(query.getId()).isEqualTo(1L);
        assertThat(query.getTemplateName()).isEqualTo("index.vue");
        assertThat(query.getPageNum()).isEqualTo(1);
        assertThat(query.getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("GenTemplatePageQuery 初始值应为 null")
    void genTemplatePageQuery_shouldHaveNullInitialValues() {
        GenTemplatePageQuery query = new GenTemplatePageQuery();

        assertThat(query.getId()).isNull();
        assertThat(query.getPageNum()).isNull();
        assertThat(query.getPageSize()).isNull();
    }
}
