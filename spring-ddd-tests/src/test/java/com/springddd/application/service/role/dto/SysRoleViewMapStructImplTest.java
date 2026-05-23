package com.springddd.application.service.role.dto;

import com.springddd.domain.role.DataPermission;
import com.springddd.infrastructure.persistence.entity.SysRoleEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SysRoleViewMapStructImplTest {

    private final SysRoleViewMapStructImpl impl = new SysRoleViewMapStructImpl();

    @Test
    @DisplayName("toView 应将 Entity 映射为 View")
    void toView_shouldMapEntityToView() {
        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleName("Admin");
        entity.setRoleCode("admin");
        entity.setRoleDesc("Admin role");
        entity.setDataScope(1);
        entity.setDataPermission("{\"rowScope\":{\"deptIds\":[],\"postIds\":[],\"userIds\":[],\"self\":false},\"columnRules\":[],\"dataScope\":1,\"deptIds\":[]}");
        entity.setRoleStatus(true);
        entity.setOwnerStatus(false);
        entity.setDeptId(1L);
        entity.setCreateBy("admin");
        entity.setUpdateBy("admin");
        entity.setDeleteStatus(false);
        entity.setVersion(1);

        SysRoleView view = impl.toView(entity);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getRoleName()).isEqualTo("Admin");
        assertThat(view.getRoleCode()).isEqualTo("admin");
        assertThat(view.getRoleDesc()).isEqualTo("Admin role");
        assertThat(view.getDataScope()).isEqualTo(1);
        assertThat(view.getDataPermission()).isNotNull();
        assertThat(view.getRoleStatus()).isTrue();
        assertThat(view.getOwnerStatus()).isFalse();
        assertThat(view.getDeptId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("toViewList 应将 Entity 列表映射为 View 列表")
    void toViewList_shouldMapEntityListToViewList() {
        SysRoleEntity e1 = new SysRoleEntity();
        e1.setId(1L);
        e1.setRoleName("Role1");

        SysRoleEntity e2 = new SysRoleEntity();
        e2.setId(2L);
        e2.setRoleName("Role2");

        List<SysRoleView> views = impl.toViewList(List.of(e1, e2));

        assertThat(views).hasSize(2);
        assertThat(views.get(0).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("toDataPermission 当 dataPermission 为 null 时应返回 null")
    void toDataPermission_whenNull_shouldReturnNull() {
        assertThat(impl.toDataPermission(null)).isNull();
    }

    @Test
    @DisplayName("toDataPermission 当 dataPermission 为空字符串时应返回 null")
    void toDataPermission_whenEmpty_shouldReturnNull() {
        assertThat(impl.toDataPermission("")).isNull();
    }

    @Test
    @DisplayName("toDataPermission 当 dataPermission 为合法 JSON 时应解析为 DataPermission")
    void toDataPermission_whenValidJson_shouldReturnDataPermission() {
        String json = "{\"rowScope\":{\"deptIds\":[],\"postIds\":[],\"userIds\":[],\"self\":false},\"columnRules\":[],\"dataScope\":1,\"deptIds\":[]}";
        DataPermission dp = impl.toDataPermission(json);
        assertThat(dp).isNotNull();
        assertThat(dp.dataScope()).isEqualTo(1);
    }

    @Test
    @DisplayName("toDataPermission 当 dataPermission 为非法 JSON 时应返回 null")
    void toDataPermission_whenInvalidJson_shouldReturnNull() {
        assertThat(impl.toDataPermission("not json")).isNull();
    }
}
