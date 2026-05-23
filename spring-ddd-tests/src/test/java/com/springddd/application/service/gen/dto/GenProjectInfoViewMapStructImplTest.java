package com.springddd.application.service.gen.dto;

import com.springddd.infrastructure.persistence.entity.GenProjectInfoEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GenProjectInfoViewMapStructImplTest {

    private final GenProjectInfoViewMapStructImpl impl = new GenProjectInfoViewMapStructImpl();

    @Test
    @DisplayName("toView 应将 Entity 映射为 View")
    void toView_shouldMapEntityToView() {
        GenProjectInfoEntity entity = new GenProjectInfoEntity();
        entity.setId(1L);
        entity.setTableName("t_user");
        entity.setPackageName("com.test");
        entity.setClassName("User");
        entity.setRequestName("UserRequest");
        entity.setModuleName("sys");
        entity.setProjectName("TestProject");
        entity.setDeleteStatus(false);
        entity.setCreateBy("admin");
        entity.setUpdateBy("admin");
        entity.setVersion(1);

        GenProjectInfoView view = impl.toView(entity);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getTableName()).isEqualTo("t_user");
        assertThat(view.getPackageName()).isEqualTo("com.test");
        assertThat(view.getClassName()).isEqualTo("User");
        assertThat(view.getRequestName()).isEqualTo("UserRequest");
        assertThat(view.getModuleName()).isEqualTo("sys");
        assertThat(view.getProjectName()).isEqualTo("TestProject");
    }

    @Test
    @DisplayName("toViews 应将 Entity 列表映射为 View 列表")
    void toViews_shouldMapEntityListToViewList() {
        GenProjectInfoEntity e1 = new GenProjectInfoEntity();
        e1.setId(1L);
        e1.setTableName("t1");

        List<GenProjectInfoView> views = impl.toViews(List.of(e1));
        assertThat(views).hasSize(1);
    }
}
