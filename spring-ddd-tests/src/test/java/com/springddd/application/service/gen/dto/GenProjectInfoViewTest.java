package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenProjectInfoViewTest {

    @Test
    @DisplayName("GenProjectInfoView 应支持无参构造和 setter/getter")
    void genProjectInfoView_shouldSupportGetterSetter() {
        GenProjectInfoView view = new GenProjectInfoView();
        view.setId(1L);
        view.setTableName("sys_user");
        view.setPackageName("com.springddd");
        view.setClassName("SysUser");
        view.setModuleName("user");
        view.setProjectName("spring-ddd");
        view.setRequestName("sys-user");

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getTableName()).isEqualTo("sys_user");
        assertThat(view.getPackageName()).isEqualTo("com.springddd");
        assertThat(view.getClassName()).isEqualTo("SysUser");
        assertThat(view.getModuleName()).isEqualTo("user");
        assertThat(view.getProjectName()).isEqualTo("spring-ddd");
        assertThat(view.getRequestName()).isEqualTo("sys-user");
    }

    @Test
    @DisplayName("GenProjectInfoView 初始值应为 null")
    void genProjectInfoView_shouldHaveNullInitialValues() {
        GenProjectInfoView view = new GenProjectInfoView();

        assertThat(view.getId()).isNull();
        assertThat(view.getTableName()).isNull();
        assertThat(view.getPackageName()).isNull();
        assertThat(view.getClassName()).isNull();
        assertThat(view.getModuleName()).isNull();
        assertThat(view.getProjectName()).isNull();
        assertThat(view.getRequestName()).isNull();
    }
}
