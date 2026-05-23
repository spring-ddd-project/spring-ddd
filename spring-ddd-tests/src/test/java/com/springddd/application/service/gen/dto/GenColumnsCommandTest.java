package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenColumnsCommandTest {

    @Test
    @DisplayName("GenColumnsCommand 应支持无参构造和 setter/getter")
    void genColumnsCommand_shouldSupportGetterSetter() {
        GenColumnsCommand command = new GenColumnsCommand();
        command.setId(1L);
        command.setInfoId(2L);
        command.setPropColumnKey("username");
        command.setPropColumnName("用户名");
        command.setPropColumnType("varchar");
        command.setFormVisible(true);
        command.setFormRequired(true);
        command.setEn("Username");
        command.setLocale("用户名");

        assertThat(command.getId()).isEqualTo(1L);
        assertThat(command.getInfoId()).isEqualTo(2L);
        assertThat(command.getPropColumnKey()).isEqualTo("username");
        assertThat(command.getPropColumnName()).isEqualTo("用户名");
        assertThat(command.getPropColumnType()).isEqualTo("varchar");
        assertThat(command.getFormVisible()).isTrue();
        assertThat(command.getFormRequired()).isTrue();
        assertThat(command.getEn()).isEqualTo("Username");
        assertThat(command.getLocale()).isEqualTo("用户名");
    }

    @Test
    @DisplayName("GenColumnsCommand 初始值应为 null")
    void genColumnsCommand_shouldHaveNullInitialValues() {
        GenColumnsCommand command = new GenColumnsCommand();

        assertThat(command.getId()).isNull();
        assertThat(command.getInfoId()).isNull();
        assertThat(command.getPropColumnKey()).isNull();
        assertThat(command.getFormVisible()).isNull();
        assertThat(command.getEn()).isNull();
    }
}
