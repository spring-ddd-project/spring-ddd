package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenColumnBindCommandTest {

    @Test
    @DisplayName("GenColumnBindCommand 应支持无参构造和 setter/getter")
    void genColumnBindCommand_shouldSupportGetterSetter() {
        GenColumnBindCommand command = new GenColumnBindCommand();
        command.setId(1L);
        command.setColumnType("varchar");
        command.setEntityType("String");
        command.setComponentType((byte) 1);
        command.setTypescriptType((byte) 2);

        assertThat(command.getId()).isEqualTo(1L);
        assertThat(command.getColumnType()).isEqualTo("varchar");
        assertThat(command.getEntityType()).isEqualTo("String");
        assertThat(command.getComponentType()).isEqualTo((byte) 1);
        assertThat(command.getTypescriptType()).isEqualTo((byte) 2);
    }

    @Test
    @DisplayName("GenColumnBindCommand 初始值应为 null")
    void genColumnBindCommand_shouldHaveNullInitialValues() {
        GenColumnBindCommand command = new GenColumnBindCommand();

        assertThat(command.getId()).isNull();
        assertThat(command.getColumnType()).isNull();
        assertThat(command.getEntityType()).isNull();
        assertThat(command.getComponentType()).isNull();
        assertThat(command.getTypescriptType()).isNull();
    }
}
