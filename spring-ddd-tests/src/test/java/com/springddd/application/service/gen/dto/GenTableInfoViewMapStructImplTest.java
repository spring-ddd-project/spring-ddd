package com.springddd.application.service.gen.dto;

import com.springddd.infrastructure.persistence.entity.GenTableInfoEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GenTableInfoViewMapStructImplTest {

    private final GenTableInfoViewMapStructImpl impl = new GenTableInfoViewMapStructImpl();

    @Test
    @DisplayName("toView 应将 Entity 映射为 View")
    void toView_shouldMapEntityToView() {
        GenTableInfoEntity entity = new GenTableInfoEntity();
        entity.setTableName("t_user");
        entity.setTableComment("用户表");
        entity.setTableCollation("utf8mb4");

        GenTableInfoView view = impl.toView(entity);

        assertThat(view.getTableName()).isEqualTo("t_user");
        assertThat(view.getTableComment()).isEqualTo("用户表");
        assertThat(view.getTableCollation()).isEqualTo("utf8mb4");
    }

    @Test
    @DisplayName("toViews 应将 Entity 列表映射为 View 列表")
    void toViews_shouldMapEntityListToViewList() {
        GenTableInfoEntity e1 = new GenTableInfoEntity();
        e1.setTableName("t1");
        e1.setTableComment("表1");

        List<GenTableInfoView> views = impl.toViews(List.of(e1));
        assertThat(views).hasSize(1);
    }
}
