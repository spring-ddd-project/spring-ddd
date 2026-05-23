package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenProjectInfoCommandTest {

    @Test
    @DisplayName("GenProjectInfoCommand 应支持无参构造和 setter/getter")
    void genProjectInfoCommand_shouldSupportGetterSetter() {
        GenProjectInfoCommand command = new GenProjectInfoCommand();
        command.setId(1L);
        command.setTableName("sys_user");
        command.setPackageName("com.springddd");
        command.setClassName("SysUser");
        command.setModuleName("user");
        command.setProjectName("spring-ddd");
        command.setRequestName("sys-user");

        assertThat(command.getId()).isEqualTo(1L);
        assertThat(command.getTableName()).isEqualTo("sys_user");
        assertThat(command.getPackageName()).isEqualTo("com.springddd");
        assertThat(command.getClassName()).isEqualTo("SysUser");
        assertThat(command.getModuleName()).isEqualTo("user");
        assertThat(command.getProjectName()).isEqualTo("spring-ddd");
        assertThat(command.getRequestName()).isEqualTo("sys-user");
    }

    @Test
    @DisplayName("GenProjectInfoCommand 初始值应为 null")
    void genProjectInfoCommand_shouldHaveNullInitialValues() {
        GenProjectInfoCommand command = new GenProjectInfoCommand();

        assertThat(command.getId()).isNull();
        assertThat(command.getTableName()).isNull();
        assertThat(command.getPackageName()).isNull();
        assertThat(command.getClassName()).isNull();
        assertThat(command.getModuleName()).isNull();
        assertThat(command.getProjectName()).isNull();
        assertThat(command.getRequestName()).isNull();
    }
}
