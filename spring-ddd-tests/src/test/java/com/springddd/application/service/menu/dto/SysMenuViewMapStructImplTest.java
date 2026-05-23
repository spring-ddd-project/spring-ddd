package com.springddd.application.service.menu.dto;

import com.springddd.infrastructure.persistence.entity.SysMenuEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SysMenuViewMapStructImplTest {

    private final SysMenuViewMapStructImpl impl = new SysMenuViewMapStructImpl();

    @Test
    @DisplayName("toView 应将 Entity 映射为 View")
    void toView_shouldMapEntityToView() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setParentId(2L);
        entity.setName("menu");
        entity.setPath("/path");
        entity.setComponent("comp");
        entity.setApi("/api");
        entity.setRedirect("/redirect");
        entity.setPermission("perm");
        entity.setSortOrder(1);
        entity.setTitle("title");
        entity.setAffixTab(true);
        entity.setNoBasicLayout(false);
        entity.setIcon("icon");
        entity.setMenuType(1);
        entity.setVisible(true);

        SysMenuView view = impl.toView(entity);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getParentId()).isEqualTo(2L);
        assertThat(view.getName()).isEqualTo("menu");
        assertThat(view.getPath()).isEqualTo("/path");
        assertThat(view.getComponent()).isEqualTo("comp");
        assertThat(view.getApi()).isEqualTo("/api");
        assertThat(view.getRedirect()).isEqualTo("/redirect");
        assertThat(view.getPermission()).isEqualTo("perm");
        assertThat(view.getMenuType()).isEqualTo(1);
        assertThat(view.getVisible()).isTrue();
        assertThat(view.getMeta()).isNotNull();
        assertThat(view.getMeta().getOrder()).isEqualTo(1);
        assertThat(view.getMeta().getTitle()).isEqualTo("title");
        assertThat(view.getMeta().getAffixTab()).isTrue();
        assertThat(view.getMeta().getNoBasicLayout()).isFalse();
        assertThat(view.getMeta().getIcon()).isEqualTo("icon");
    }

    @Test
    @DisplayName("toViewList 应将 Entity 列表映射为 View 列表")
    void toViewList_shouldMapEntityListToViewList() {
        SysMenuEntity e1 = new SysMenuEntity();
        e1.setId(1L);
        e1.setName("Menu1");

        SysMenuEntity e2 = new SysMenuEntity();
        e2.setId(2L);
        e2.setName("Menu2");

        List<SysMenuView> views = impl.toViewList(List.of(e1, e2));

        assertThat(views).hasSize(2);
        assertThat(views.get(0).getId()).isEqualTo(1L);
        assertThat(views.get(1).getId()).isEqualTo(2L);
    }
}
