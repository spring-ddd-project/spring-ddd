package com.springddd.application.service.user.dto;

import com.springddd.infrastructure.persistence.entity.SysUserRoleEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SysUserRoleViewMapStructImplTest {

    private final SysUserRoleViewMapStructImpl impl = new SysUserRoleViewMapStructImpl();

    @Test
    @DisplayName("toView 应将 Entity 映射为 View")
    void toView_shouldMapEntityToView() {
        SysUserRoleEntity entity = new SysUserRoleEntity();
        entity.setId(1L);
        entity.setUserId(2L);
        entity.setRoleId(3L);
        entity.setDeptId(4L);
        entity.setCreateBy("admin");
        entity.setUpdateBy("admin");
        entity.setDeleteStatus(false);
        entity.setVersion(1);

        SysUserRoleView view = impl.toView(entity);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getUserId()).isEqualTo(2L);
        assertThat(view.getRoleId()).isEqualTo(3L);
        assertThat(view.getDeptId()).isEqualTo(4L);
    }

    @Test
    @DisplayName("toViewList 应将 Entity 列表映射为 View 列表")
    void toViewList_shouldMapEntityListToViewList() {
        SysUserRoleEntity e1 = new SysUserRoleEntity();
        e1.setId(1L);
        e1.setUserId(2L);

        List<SysUserRoleView> views = impl.toViewList(List.of(e1));

        assertThat(views).hasSize(1);
        assertThat(views.get(0).getId()).isEqualTo(1L);
    }
}
