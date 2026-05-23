package com.springddd.application.service.dict.dto;

import com.springddd.infrastructure.persistence.entity.SysDictItemEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SysDictItemViewMapStructImplTest {

    private final SysDictItemViewMapStructImpl impl = new SysDictItemViewMapStructImpl();

    @Test
    @DisplayName("toView 应将 Entity 映射为 View")
    void toView_shouldMapEntityToView() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        entity.setDictId(2L);
        entity.setItemLabel("Label");
        entity.setItemValue(100);
        entity.setSortOrder(1);
        entity.setItemStatus(true);
        entity.setDeleteStatus(false);
        entity.setCreateBy("admin");
        entity.setUpdateBy("admin");
        entity.setVersion(1);

        SysDictItemView view = impl.toView(entity);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getDictId()).isEqualTo(2L);
        assertThat(view.getItemLabel()).isEqualTo("Label");
        assertThat(view.getItemValue()).isEqualTo(100);
    }

    @Test
    @DisplayName("toViews 应将 Entity 列表映射为 View 列表")
    void toViews_shouldMapEntityListToViewList() {
        SysDictItemEntity e1 = new SysDictItemEntity();
        e1.setId(1L);
        e1.setItemLabel("Item1");

        SysDictItemEntity e2 = new SysDictItemEntity();
        e2.setId(2L);
        e2.setItemLabel("Item2");

        List<SysDictItemView> views = impl.toViews(List.of(e1, e2));

        assertThat(views).hasSize(2);
        assertThat(views.get(0).getId()).isEqualTo(1L);
    }
}
