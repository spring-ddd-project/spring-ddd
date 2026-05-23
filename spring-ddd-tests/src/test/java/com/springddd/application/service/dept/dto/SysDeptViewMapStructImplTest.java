package com.springddd.application.service.dept.dto;

import com.springddd.infrastructure.persistence.entity.SysDeptEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SysDeptViewMapStructImplTest {

    private final SysDeptViewMapStructImpl impl = new SysDeptViewMapStructImpl();

    @Test
    @DisplayName("toView 应将 Entity 映射为 View")
    void toView_shouldMapEntityToView() {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setId(1L);
        entity.setParentId(2L);
        entity.setDeptName("TestDept");
        entity.setSortOrder(3);
        entity.setDeptStatus(true);
        entity.setDeleteStatus(false);
        entity.setCreateBy("admin");
        entity.setUpdateBy("admin");
        entity.setVersion(1);

        SysDeptView view = impl.toView(entity);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getParentId()).isEqualTo(2L);
        assertThat(view.getDeptName()).isEqualTo("TestDept");
        assertThat(view.getSortOrder()).isEqualTo(3);
        assertThat(view.getDeptStatus()).isTrue();
        assertThat(view.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("toViews 应将 Entity 列表映射为 View 列表")
    void toViews_shouldMapEntityListToViewList() {
        SysDeptEntity e1 = new SysDeptEntity();
        e1.setId(1L);
        e1.setDeptName("Dept1");

        SysDeptEntity e2 = new SysDeptEntity();
        e2.setId(2L);
        e2.setDeptName("Dept2");

        List<SysDeptView> views = impl.toViews(List.of(e1, e2));

        assertThat(views).hasSize(2);
        assertThat(views.get(0).getId()).isEqualTo(1L);
        assertThat(views.get(1).getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("toViews 应处理空列表")
    void toViews_shouldHandleEmptyList() {
        List<SysDeptView> views = impl.toViews(List.of());
        assertThat(views).isEmpty();
    }
}
