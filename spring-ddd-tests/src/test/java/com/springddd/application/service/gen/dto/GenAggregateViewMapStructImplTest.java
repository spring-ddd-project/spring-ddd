package com.springddd.application.service.gen.dto;

import com.springddd.infrastructure.persistence.entity.GenAggregateEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GenAggregateViewMapStructImplTest {

    private final GenAggregateViewMapStructImpl impl = new GenAggregateViewMapStructImpl();

    @Test
    @DisplayName("toView 应将 Entity 映射为 View")
    void toView_shouldMapEntityToView() {
        GenAggregateEntity entity = new GenAggregateEntity();
        entity.setId(1L);
        entity.setInfoId(2L);
        entity.setObjectName("User");
        entity.setObjectValue("user");
        entity.setObjectType((byte) 1);
        entity.setHasCreated(true);
        entity.setCreateBy("admin");
        entity.setUpdateBy("admin");
        entity.setVersion(1);

        GenAggregateView view = impl.toView(entity);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getInfoId()).isEqualTo(2L);
        assertThat(view.getObjectName()).isEqualTo("User");
        assertThat(view.getObjectValue()).isEqualTo("user");
        assertThat(view.getObjectType()).isEqualTo((byte) 1);
        assertThat(view.getHasCreated()).isTrue();
    }

    @Test
    @DisplayName("toViews 应将 Entity 列表映射为 View 列表")
    void toViews_shouldMapEntityListToViewList() {
        GenAggregateEntity e1 = new GenAggregateEntity();
        e1.setId(1L);
        e1.setObjectName("Obj1");

        List<GenAggregateView> views = impl.toViews(List.of(e1));
        assertThat(views).hasSize(1);
        assertThat(views.get(0).getId()).isEqualTo(1L);
    }
}
