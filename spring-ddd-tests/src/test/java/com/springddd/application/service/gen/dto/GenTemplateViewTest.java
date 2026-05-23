package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenTemplateViewTest {

    @Test
    @DisplayName("GenTemplateView 应支持无参构造和 setter/getter")
    void genTemplateView_shouldSupportGetterSetter() {
        GenTemplateView view = new GenTemplateView();
        view.setId(1L);
        view.setTemplateName("index.vue");
        view.setTemplateContent("<template></template>");

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getTemplateName()).isEqualTo("index.vue");
        assertThat(view.getTemplateContent()).isEqualTo("<template></template>");
    }

    @Test
    @DisplayName("GenTemplateView 初始值应为 null")
    void genTemplateView_shouldHaveNullInitialValues() {
        GenTemplateView view = new GenTemplateView();

        assertThat(view.getId()).isNull();
        assertThat(view.getTemplateName()).isNull();
        assertThat(view.getTemplateContent()).isNull();
    }
}
