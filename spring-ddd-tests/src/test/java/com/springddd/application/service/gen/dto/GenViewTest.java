package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GenViewTest {

    @Test
    @DisplayName("GenView 应支持无参构造和 setter/getter")
    void genView_shouldSupportGetterSetter() {
        GenProjectInfoView projectInfoView = new GenProjectInfoView();
        projectInfoView.setTableName("sys_user");

        GenColumnsView columnsView = new GenColumnsView();
        columnsView.setPropColumnKey("username");

        GenAggregateView aggregateView = new GenAggregateView();
        aggregateView.setObjectName("SysUser");

        GenView genView = new GenView();
        genView.setProjectInfoView(projectInfoView);
        genView.setColumnsViews(List.of(columnsView));
        genView.setAggregateViews(List.of(aggregateView));

        assertThat(genView.getProjectInfoView()).isEqualTo(projectInfoView);
        assertThat(genView.getColumnsViews()).hasSize(1);
        assertThat(genView.getAggregateViews()).hasSize(1);
    }

    @Test
    @DisplayName("GenView 初始值应为 null")
    void genView_shouldHaveNullInitialValues() {
        GenView genView = new GenView();

        assertThat(genView.getProjectInfoView()).isNull();
        assertThat(genView.getColumnsViews()).isNull();
        assertThat(genView.getAggregateViews()).isNull();
    }
}
