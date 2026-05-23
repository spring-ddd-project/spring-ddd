package com.springddd.application.service.role.dto;

import com.springddd.infrastructure.persistence.entity.SysRoleMenuEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SysRoleMenuViewMapStructImplTest {

    private final SysRoleMenuViewMapStructImpl impl = new SysRoleMenuViewMapStructImpl();

    @Test
    @DisplayName("toView 应将 Entity 映射为 View")
    void toView_shouldMapEntityToView() {
        SysRoleMenuEntity entity = new SysRoleMenuEntity();
        entity.setId(1L);
        entity.setRoleId(2L);
        entity.setMenuId(3L);
        entity.setDeptId(4L);
        entity.setDeleteStatus(false);
        entity.setCreateBy("admin");
        entity.setUpdateBy("admin");
        entity.setVersion(1);

        SysRoleMenuView view = impl.toView(entity);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getRoleId()).isEqualTo(2L);
        assertThat(view.getMenuId()).isEqualTo(3L);
        assertThat(view.getDeptId()).isEqualTo(4L);
    }

    @Test
    @DisplayName("toViewList 应将 Entity 列表映射为 View 列表")
    void toViewList_shouldMapEntityListToViewList() {
        SysRoleMenuEntity e1 = new SysRoleMenuEntity();
        e1.setId(1L);
        e1.setRoleId(2L);

        List<SysRoleMenuView> views = impl.toViewList(List.of(e1));

        assertThat(views).hasSize(1);
        assertThat(views.get(0).getId()).isEqualTo(1L);
    }
}
