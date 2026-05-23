package com.springddd.application.service.gen.dto;

import com.springddd.infrastructure.persistence.entity.GenColumnBindEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GenColumnBindViewMapStructImplTest {

    private final GenColumnBindViewMapStructImpl impl = new GenColumnBindViewMapStructImpl();

    @Test
    @DisplayName("toView 应将 Entity 映射为 View")
    void toView_shouldMapEntityToView() {
        GenColumnBindEntity entity = new GenColumnBindEntity();
        entity.setId(1L);
        entity.setColumnType("varchar");
        entity.setEntityType("String");
        entity.setComponentType((byte) 1);
        entity.setTypescriptType((byte) 2);
        entity.setDeleteStatus(false);
        entity.setCreateBy("admin");
        entity.setUpdateBy("admin");
        entity.setVersion(1);

        GenColumnBindView view = impl.toView(entity);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getColumnType()).isEqualTo("varchar");
        assertThat(view.getEntityType()).isEqualTo("String");
        assertThat(view.getComponentType()).isEqualTo((byte) 1);
        assertThat(view.getTypescriptType()).isEqualTo((byte) 2);
    }

    @Test
    @DisplayName("toViews 应将 Entity 列表映射为 View 列表")
    void toViews_shouldMapEntityListToViewList() {
        GenColumnBindEntity e1 = new GenColumnBindEntity();
        e1.setId(1L);
        e1.setColumnType("type1");

        List<GenColumnBindView> views = impl.toViews(List.of(e1));
        assertThat(views).hasSize(1);
    }
}
