package com.springddd.application.service.gen.dto;

import com.springddd.infrastructure.persistence.entity.GenColumnsEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GenColumnsViewMapStructImplTest {

    private final GenColumnsViewMapStructImpl impl = new GenColumnsViewMapStructImpl();

    @Test
    @DisplayName("toView 应将 Entity 映射为 View")
    void toView_shouldMapEntityToView() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setId(1L);
        entity.setInfoId(2L);
        entity.setPropColumnKey("id");
        entity.setPropColumnName("主键");
        entity.setPropColumnType("bigint");
        entity.setPropColumnComment("主键注释");
        entity.setPropJavaEntity("Id");
        entity.setPropJavaType("Long");
        entity.setPropDictId(3L);
        entity.setTableVisible(true);
        entity.setTableOrder(true);
        entity.setTableFilter(true);
        entity.setTableFilterComponent((byte) 1);
        entity.setTableFilterType((byte) 2);
        entity.setTypescriptType((byte) 3);
        entity.setFormComponent((byte) 4);
        entity.setFormVisible(true);
        entity.setFormRequired(true);
        entity.setEn("en");
        entity.setLocale("zh");

        GenColumnsView view = impl.toView(entity);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getInfoId()).isEqualTo(2L);
        assertThat(view.getPropColumnKey()).isEqualTo("id");
        assertThat(view.getPropColumnName()).isEqualTo("主键");
        assertThat(view.getPropColumnType()).isEqualTo("bigint");
        assertThat(view.getPropJavaType()).isEqualTo("Long");
        assertThat(view.getTableVisible()).isTrue();
        assertThat(view.getFormRequired()).isTrue();
    }

    @Test
    @DisplayName("toViews 应将 Entity 列表映射为 View 列表")
    void toViews_shouldMapEntityListToViewList() {
        GenColumnsEntity e1 = new GenColumnsEntity();
        e1.setId(1L);
        e1.setPropColumnKey("col1");

        List<GenColumnsView> views = impl.toViews(List.of(e1));
        assertThat(views).hasSize(1);
    }
}
