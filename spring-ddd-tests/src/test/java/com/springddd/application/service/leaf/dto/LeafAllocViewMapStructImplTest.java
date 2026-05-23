package com.springddd.application.service.leaf.dto;

import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LeafAllocViewMapStructImplTest {

    private final LeafAllocViewMapStructImpl impl = new LeafAllocViewMapStructImpl();

    @Test
    @DisplayName("toView 应将 Entity 映射为 View")
    void toView_shouldMapEntityToView() {
        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setId(1L);
        entity.setBizTag("test");
        entity.setMaxId(100L);
        entity.setStep(10);
        entity.setDescription("desc");
        entity.setVersion(1);
        entity.setDeleteStatus(false);
        entity.setCreateBy("admin");
        entity.setUpdateBy("admin");
        entity.setDeptId(1L);

        LeafAllocView view = impl.toView(entity);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getBizTag()).isEqualTo("test");
        assertThat(view.getMaxId()).isEqualTo(100L);
        assertThat(view.getStep()).isEqualTo(10);
        assertThat(view.getDescription()).isEqualTo("desc");
    }

    @Test
    @DisplayName("toViews 应将 Entity 列表映射为 View 列表")
    void toViews_shouldMapEntityListToViewList() {
        LeafAllocEntity e1 = new LeafAllocEntity();
        e1.setId(1L);
        e1.setBizTag("tag1");

        LeafAllocEntity e2 = new LeafAllocEntity();
        e2.setId(2L);
        e2.setBizTag("tag2");

        List<LeafAllocView> views = impl.toViews(List.of(e1, e2));

        assertThat(views).hasSize(2);
        assertThat(views.get(0).getId()).isEqualTo(1L);
    }
}
