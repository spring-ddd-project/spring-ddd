package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenProjectInfoDTOTest {

    @Test
    @DisplayName("GenProjectInfoDTO 应支持无参构造和 setter/getter")
    void genProjectInfoDTO_shouldSupportGetterSetter() {
        GenProjectInfoDTO dto = new GenProjectInfoDTO();
        dto.setTableName("sys_user");
        dto.setPackageName("com.springddd");
        dto.setClassName("SysUser");
        dto.setModuleName("user");
        dto.setProjectName("spring-ddd");
        dto.setRequestName("sys-user");

        assertThat(dto.getTableName()).isEqualTo("sys_user");
        assertThat(dto.getPackageName()).isEqualTo("com.springddd");
        assertThat(dto.getClassName()).isEqualTo("SysUser");
        assertThat(dto.getModuleName()).isEqualTo("user");
        assertThat(dto.getProjectName()).isEqualTo("spring-ddd");
        assertThat(dto.getRequestName()).isEqualTo("sys-user");
    }

    @Test
    @DisplayName("GenProjectInfoDTO 初始值应为 null")
    void genProjectInfoDTO_shouldHaveNullInitialValues() {
        GenProjectInfoDTO dto = new GenProjectInfoDTO();

        assertThat(dto.getTableName()).isNull();
        assertThat(dto.getPackageName()).isNull();
        assertThat(dto.getClassName()).isNull();
        assertThat(dto.getModuleName()).isNull();
        assertThat(dto.getProjectName()).isNull();
        assertThat(dto.getRequestName()).isNull();
    }
}
