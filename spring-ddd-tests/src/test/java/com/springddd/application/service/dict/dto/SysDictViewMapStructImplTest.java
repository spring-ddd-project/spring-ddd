package com.springddd.application.service.dict.dto;

import com.springddd.infrastructure.persistence.entity.SysDictEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SysDictViewMapStructImplTest {

    private final SysDictViewMapStructImpl impl = new SysDictViewMapStructImpl();

    @Test
    @DisplayName("toView 应将 Entity 映射为 View")
    void toView_shouldMapEntityToView() {
        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictName("TestDict");
        entity.setDictCode("test_dict");
        entity.setSortOrder(1);
        entity.setDictStatus(true);
        entity.setDeleteStatus(false);
        entity.setCreateBy("admin");
        entity.setUpdateBy("admin");
        entity.setVersion(1);

        SysDictView view = impl.toView(entity);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getDictName()).isEqualTo("TestDict");
        assertThat(view.getDictCode()).isEqualTo("test_dict");
    }

    @Test
    @DisplayName("toViews 应将 Entity 列表映射为 View 列表")
    void toViews_shouldMapEntityListToViewList() {
        SysDictEntity e1 = new SysDictEntity();
        e1.setId(1L);
        e1.setDictName("Dict1");

        SysDictEntity e2 = new SysDictEntity();
        e2.setId(2L);
        e2.setDictName("Dict2");

        List<SysDictView> views = impl.toViews(List.of(e1, e2));

        assertThat(views).hasSize(2);
        assertThat(views.get(0).getId()).isEqualTo(1L);
        assertThat(views.get(1).getId()).isEqualTo(2L);
    }
}
