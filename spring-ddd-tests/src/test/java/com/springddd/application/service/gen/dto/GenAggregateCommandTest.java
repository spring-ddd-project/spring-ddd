package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenAggregateCommandTest {

    @Test
    @DisplayName("GenAggregateCommand 应支持无参构造和 setter/getter")
    void genAggregateCommand_shouldSupportGetterSetter() {
        GenAggregateCommand command = new GenAggregateCommand();
        command.setId(1L);
        command.setInfoId(2L);
        command.setObjectName("SysUser");
        command.setObjectValue("user");
        command.setObjectType((byte) 1);
        command.setHasCreated(true);

        assertThat(command.getId()).isEqualTo(1L);
        assertThat(command.getInfoId()).isEqualTo(2L);
        assertThat(command.getObjectName()).isEqualTo("SysUser");
        assertThat(command.getObjectValue()).isEqualTo("user");
        assertThat(command.getObjectType()).isEqualTo((byte) 1);
        assertThat(command.getHasCreated()).isTrue();
    }

    @Test
    @DisplayName("GenAggregateCommand 初始值应为 null")
    void genAggregateCommand_shouldHaveNullInitialValues() {
        GenAggregateCommand command = new GenAggregateCommand();

        assertThat(command.getId()).isNull();
        assertThat(command.getInfoId()).isNull();
        assertThat(command.getObjectName()).isNull();
        assertThat(command.getObjectValue()).isNull();
        assertThat(command.getObjectType()).isNull();
        assertThat(command.getHasCreated()).isNull();
    }
}
