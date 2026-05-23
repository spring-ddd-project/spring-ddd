package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenTemplateCommandTest {

    @Test
    @DisplayName("GenTemplateCommand 应支持无参构造和 setter/getter")
    void genTemplateCommand_shouldSupportGetterSetter() {
        GenTemplateCommand command = new GenTemplateCommand();
        command.setId(1L);
        command.setTemplateName("index.vue");
        command.setTemplateContent("<template></template>");

        assertThat(command.getId()).isEqualTo(1L);
        assertThat(command.getTemplateName()).isEqualTo("index.vue");
        assertThat(command.getTemplateContent()).isEqualTo("<template></template>");
    }

    @Test
    @DisplayName("GenTemplateCommand 初始值应为 null")
    void genTemplateCommand_shouldHaveNullInitialValues() {
        GenTemplateCommand command = new GenTemplateCommand();

        assertThat(command.getId()).isNull();
        assertThat(command.getTemplateName()).isNull();
        assertThat(command.getTemplateContent()).isNull();
    }
}
