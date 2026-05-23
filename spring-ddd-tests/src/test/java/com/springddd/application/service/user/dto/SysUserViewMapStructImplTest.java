package com.springddd.application.service.user.dto;

import com.springddd.infrastructure.persistence.entity.SysUserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SysUserViewMapStructImplTest {

    private final SysUserViewMapStructImpl impl = new SysUserViewMapStructImpl();

    @Test
    @DisplayName("toView 应将 Entity 映射为 View")
    void toView_shouldMapEntityToView() {
        SysUserEntity entity = new SysUserEntity();
        entity.setId(1L);
        entity.setUsername("admin");
        entity.setPassword("pass");
        entity.setPhone("123456");
        entity.setAvatar("avatar.jpg");
        entity.setEmail("a@b.com");
        entity.setSex(true);
        entity.setLockStatus(false);
        entity.setDeptId(1L);
        entity.setCreateBy("admin");
        entity.setUpdateBy("admin");
        entity.setDeleteStatus(false);
        entity.setVersion(1);

        SysUserView view = impl.toView(entity);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getUsername()).isEqualTo("admin");
        assertThat(view.getPhone()).isEqualTo("123456");
        assertThat(view.getEmail()).isEqualTo("a@b.com");
        assertThat(view.getSex()).isTrue();
        assertThat(view.getLockStatus()).isFalse();
        assertThat(view.getDeptId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("toViewList 应将 Entity 列表映射为 View 列表")
    void toViewList_shouldMapEntityListToViewList() {
        SysUserEntity e1 = new SysUserEntity();
        e1.setId(1L);
        e1.setUsername("User1");

        SysUserEntity e2 = new SysUserEntity();
        e2.setId(2L);
        e2.setUsername("User2");

        List<SysUserView> views = impl.toViewList(List.of(e1, e2));

        assertThat(views).hasSize(2);
        assertThat(views.get(0).getId()).isEqualTo(1L);
    }
}
