package com.springddd.application.service.gen.dto;

import com.springddd.infrastructure.persistence.entity.GenTemplateEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GenTemplateViewMapStructImplTest {

    private final GenTemplateViewMapStructImpl impl = new GenTemplateViewMapStructImpl();

    @Test
    @DisplayName("toView 应将 Entity 映射为 View")
    void toView_shouldMapEntityToView() {
        GenTemplateEntity entity = new GenTemplateEntity();
        entity.setId(1L);
        entity.setTemplateName("test");
        entity.setTemplateContent("content");
        entity.setDeleteStatus(false);
        entity.setCreateBy("admin");
        entity.setUpdateBy("admin");
        entity.setVersion(1);

        GenTemplateView view = impl.toView(entity);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getTemplateName()).isEqualTo("test");
        assertThat(view.getTemplateContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("toViews 应将 Entity 列表映射为 View 列表")
    void toViews_shouldMapEntityListToViewList() {
        GenTemplateEntity e1 = new GenTemplateEntity();
        e1.setId(1L);
        e1.setTemplateName("t1");

        List<GenTemplateView> views = impl.toViews(List.of(e1));
        assertThat(views).hasSize(1);
    }
}
