package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenProjectInfoQueryTest {

    @Test
    @DisplayName("GenProjectInfoQuery 应支持无参构造和 setter/getter")
    void genProjectInfoQuery_shouldSupportGetterSetter() {
        GenProjectInfoQuery query = new GenProjectInfoQuery();
        query.setId(1L);
        query.setTableName("sys_user");
        query.setPackageName("com.springddd");
        query.setClassName("SysUser");
        query.setModuleName("user");
        query.setProjectName("spring-ddd");
        query.setRequestName("sys-user");
        query.setDeleteStatus(false);

        assertThat(query.getId()).isEqualTo(1L);
        assertThat(query.getTableName()).isEqualTo("sys_user");
        assertThat(query.getPackageName()).isEqualTo("com.springddd");
        assertThat(query.getClassName()).isEqualTo("SysUser");
        assertThat(query.getModuleName()).isEqualTo("user");
        assertThat(query.getProjectName()).isEqualTo("spring-ddd");
        assertThat(query.getRequestName()).isEqualTo("sys-user");
        assertThat(query.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("GenProjectInfoQuery 初始值应为 null")
    void genProjectInfoQuery_shouldHaveNullInitialValues() {
        GenProjectInfoQuery query = new GenProjectInfoQuery();

        assertThat(query.getId()).isNull();
        assertThat(query.getTableName()).isNull();
        assertThat(query.getPackageName()).isNull();
        assertThat(query.getClassName()).isNull();
        assertThat(query.getModuleName()).isNull();
        assertThat(query.getProjectName()).isNull();
        assertThat(query.getRequestName()).isNull();
        assertThat(query.getDeleteStatus()).isNull();
    }
}
