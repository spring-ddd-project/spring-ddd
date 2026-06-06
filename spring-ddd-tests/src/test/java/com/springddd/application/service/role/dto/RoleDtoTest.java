package com.springddd.application.service.role.dto;

import com.springddd.domain.role.DataPermission;
import com.springddd.domain.role.RowScope;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoleDtoTest {

    @Test
    @DisplayName("SysRoleCommand 应支持无参构造和 setter/getter")
    void sysRoleCommand_shouldSupportGetterSetter() {
        SysRoleCommand command = new SysRoleCommand();
        command.setId(1L);
        command.setRoleName("TestRole");
        command.setRoleCode("test_role");
        command.setRoleDesc("Test description");
        command.setDataScope(1);
        command.setRoleStatus(true);
        command.setOwnerStatus(false);
        command.setDeptId(2L);
        command.setDeleteStatus(false);
        command.setVersion(1);

        RowScope rowScope = new RowScope(List.of(1L), List.of(2L), List.of(3L), true);
        DataPermission dataPermission = new DataPermission(rowScope, 1, List.of(1L));
        command.setDataPermission(dataPermission);

        assertThat(command.getId()).isEqualTo(1L);
        assertThat(command.getRoleName()).isEqualTo("TestRole");
        assertThat(command.getRoleCode()).isEqualTo("test_role");
        assertThat(command.getRoleDesc()).isEqualTo("Test description");
        assertThat(command.getDataScope()).isEqualTo(1);
        assertThat(command.getRoleStatus()).isTrue();
        assertThat(command.getOwnerStatus()).isFalse();
        assertThat(command.getDeptId()).isEqualTo(2L);
        assertThat(command.getDeleteStatus()).isFalse();
        assertThat(command.getVersion()).isEqualTo(1);
        assertThat(command.getDataPermission()).isEqualTo(dataPermission);
    }

    @Test
    @DisplayName("SysRoleCommand 初始值应为 null")
    void sysRoleCommand_shouldHaveNullInitialValues() {
        SysRoleCommand command = new SysRoleCommand();

        assertThat(command.getId()).isNull();
        assertThat(command.getRoleName()).isNull();
        assertThat(command.getRoleCode()).isNull();
        assertThat(command.getRoleDesc()).isNull();
        assertThat(command.getDataScope()).isNull();
        assertThat(command.getRoleStatus()).isNull();
        assertThat(command.getOwnerStatus()).isNull();
        assertThat(command.getDeptId()).isNull();
        assertThat(command.getDataPermission()).isNull();
        assertThat(command.getDeleteStatus()).isNull();
        assertThat(command.getVersion()).isNull();
    }

    @Test
    @DisplayName("SysRoleQuery 应支持无参构造和 setter/getter")
    void sysRoleQuery_shouldSupportGetterSetter() {
        SysRoleQuery query = new SysRoleQuery();
        query.setId(1L);
        query.setRoleName("TestRole");
        query.setRoleCode("test_role");
        query.setRoleDesc("Test description");
        query.setDataScope(1);
        query.setRoleStatus(true);
        query.setOwner(true);
        query.setDeptId(2L);
        query.setDeleteStatus(false);
        query.setVersion(1);

        assertThat(query.getId()).isEqualTo(1L);
        assertThat(query.getRoleName()).isEqualTo("TestRole");
        assertThat(query.getRoleCode()).isEqualTo("test_role");
        assertThat(query.getRoleDesc()).isEqualTo("Test description");
        assertThat(query.getDataScope()).isEqualTo(1);
        assertThat(query.getRoleStatus()).isTrue();
        assertThat(query.getOwner()).isTrue();
        assertThat(query.getDeptId()).isEqualTo(2L);
        assertThat(query.getDeleteStatus()).isFalse();
        assertThat(query.getVersion()).isEqualTo(1);
    }

    @Test
    @DisplayName("SysRoleQuery 应支持 FieldNameConstants")
    void sysRoleQuery_shouldSupportFieldNameConstants() {
        assertThat(SysRoleQuery.Fields.id).isEqualTo("id");
        assertThat(SysRoleQuery.Fields.roleName).isEqualTo("roleName");
        assertThat(SysRoleQuery.Fields.roleCode).isEqualTo("roleCode");
        assertThat(SysRoleQuery.Fields.roleDesc).isEqualTo("roleDesc");
        assertThat(SysRoleQuery.Fields.dataScope).isEqualTo("dataScope");
        assertThat(SysRoleQuery.Fields.roleStatus).isEqualTo("roleStatus");
        assertThat(SysRoleQuery.Fields.owner).isEqualTo("owner");
        assertThat(SysRoleQuery.Fields.deptId).isEqualTo("deptId");
        assertThat(SysRoleQuery.Fields.deleteStatus).isEqualTo("deleteStatus");
        assertThat(SysRoleQuery.Fields.version).isEqualTo("version");
    }

    @Test
    @DisplayName("SysRolePageQuery 应支持无参构造和 setter/getter")
    void sysRolePageQuery_shouldSupportGetterSetter() {
        SysRolePageQuery pageQuery = new SysRolePageQuery();
        pageQuery.setId(1L);
        pageQuery.setRoleName("TestRole");
        pageQuery.setRoleCode("test_role");
        pageQuery.setRoleDesc("Test description");
        pageQuery.setDataScope(1);
        pageQuery.setRoleStatus(true);
        pageQuery.setOwner(true);
        pageQuery.setDeptId(2L);
        pageQuery.setDeleteStatus(false);
        pageQuery.setVersion(1);
        pageQuery.setPageNum(1);
        pageQuery.setPageSize(10);

        assertThat(pageQuery.getId()).isEqualTo(1L);
        assertThat(pageQuery.getRoleName()).isEqualTo("TestRole");
        assertThat(pageQuery.getRoleCode()).isEqualTo("test_role");
        assertThat(pageQuery.getRoleDesc()).isEqualTo("Test description");
        assertThat(pageQuery.getDataScope()).isEqualTo(1);
        assertThat(pageQuery.getRoleStatus()).isTrue();
        assertThat(pageQuery.getOwner()).isTrue();
        assertThat(pageQuery.getDeptId()).isEqualTo(2L);
        assertThat(pageQuery.getDeleteStatus()).isFalse();
        assertThat(pageQuery.getVersion()).isEqualTo(1);
        assertThat(pageQuery.getPageNum()).isEqualTo(1);
        assertThat(pageQuery.getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("SysRolePageQuery 应支持 equals 和 hashCode")
    void sysRolePageQuery_shouldSupportEqualsAndHashCode() {
        SysRolePageQuery pageQuery1 = new SysRolePageQuery();
        pageQuery1.setId(1L);
        pageQuery1.setPageNum(1);
        pageQuery1.setPageSize(10);

        SysRolePageQuery pageQuery2 = new SysRolePageQuery();
        pageQuery2.setId(1L);
        pageQuery2.setPageNum(1);
        pageQuery2.setPageSize(10);

        assertThat(pageQuery1).isEqualTo(pageQuery2);
        assertThat(pageQuery1.hashCode()).isEqualTo(pageQuery2.hashCode());
    }

    @Test
    @DisplayName("SysRoleView 应支持无参构造和 setter/getter")
    void sysRoleView_shouldSupportGetterSetter() {
        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setRoleName("TestRole");
        view.setRoleCode("test_role");
        view.setRoleDesc("Test description");
        view.setDataScope(1);
        view.setRoleStatus(true);
        view.setOwnerStatus(false);
        view.setDeptId(2L);
        view.setDeleteStatus(false);
        view.setVersion(1);

        RowScope rowScope = new RowScope(List.of(1L), List.of(2L), List.of(3L), true);
        DataPermission dataPermission = new DataPermission(rowScope, 1, List.of(1L));
        view.setDataPermission(dataPermission);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getRoleName()).isEqualTo("TestRole");
        assertThat(view.getRoleCode()).isEqualTo("test_role");
        assertThat(view.getRoleDesc()).isEqualTo("Test description");
        assertThat(view.getDataScope()).isEqualTo(1);
        assertThat(view.getRoleStatus()).isTrue();
        assertThat(view.getOwnerStatus()).isFalse();
        assertThat(view.getDeptId()).isEqualTo(2L);
        assertThat(view.getDeleteStatus()).isFalse();
        assertThat(view.getVersion()).isEqualTo(1);
        assertThat(view.getDataPermission()).isEqualTo(dataPermission);
    }

    @Test
    @DisplayName("SysRoleMenuCommand 应支持无参构造和 setter/getter")
    void sysRoleMenuCommand_shouldSupportGetterSetter() {
        SysRoleMenuCommand command = new SysRoleMenuCommand();
        command.setId(1L);
        command.setRoleId(2L);
        command.setMenuId(3L);
        command.setDeptId(4L);
        command.setDeleteStatus(false);

        assertThat(command.getId()).isEqualTo(1L);
        assertThat(command.getRoleId()).isEqualTo(2L);
        assertThat(command.getMenuId()).isEqualTo(3L);
        assertThat(command.getDeptId()).isEqualTo(4L);
        assertThat(command.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("SysRoleMenuQuery 应支持无参构造和 setter/getter")
    void sysRoleMenuQuery_shouldSupportGetterSetter() {
        SysRoleMenuQuery query = new SysRoleMenuQuery();
        query.setId(1L);
        query.setRoleId(2L);
        query.setMenuId(3L);
        query.setDeptId(4L);
        query.setDeleteStatus(false);
        query.setVersion(1);

        assertThat(query.getId()).isEqualTo(1L);
        assertThat(query.getRoleId()).isEqualTo(2L);
        assertThat(query.getMenuId()).isEqualTo(3L);
        assertThat(query.getDeptId()).isEqualTo(4L);
        assertThat(query.getDeleteStatus()).isFalse();
        assertThat(query.getVersion()).isEqualTo(1);
    }

    @Test
    @DisplayName("SysRoleMenuQuery 应支持 FieldNameConstants")
    void sysRoleMenuQuery_shouldSupportFieldNameConstants() {
        assertThat(SysRoleMenuQuery.Fields.id).isEqualTo("id");
        assertThat(SysRoleMenuQuery.Fields.roleId).isEqualTo("roleId");
        assertThat(SysRoleMenuQuery.Fields.menuId).isEqualTo("menuId");
        assertThat(SysRoleMenuQuery.Fields.deptId).isEqualTo("deptId");
        assertThat(SysRoleMenuQuery.Fields.deleteStatus).isEqualTo("deleteStatus");
        assertThat(SysRoleMenuQuery.Fields.version).isEqualTo("version");
    }

    @Test
    @DisplayName("SysRoleMenuPageQuery 应支持无参构造和 setter/getter")
    void sysRoleMenuPageQuery_shouldSupportGetterSetter() {
        SysRoleMenuPageQuery pageQuery = new SysRoleMenuPageQuery();
        pageQuery.setId(1L);
        pageQuery.setRoleId(2L);
        pageQuery.setMenuId(3L);
        pageQuery.setDeptId(4L);
        pageQuery.setDeleteStatus(false);
        pageQuery.setVersion(1);
        pageQuery.setPageNum(1);
        pageQuery.setPageSize(10);

        assertThat(pageQuery.getId()).isEqualTo(1L);
        assertThat(pageQuery.getRoleId()).isEqualTo(2L);
        assertThat(pageQuery.getMenuId()).isEqualTo(3L);
        assertThat(pageQuery.getDeptId()).isEqualTo(4L);
        assertThat(pageQuery.getDeleteStatus()).isFalse();
        assertThat(pageQuery.getVersion()).isEqualTo(1);
        assertThat(pageQuery.getPageNum()).isEqualTo(1);
        assertThat(pageQuery.getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("SysRoleMenuPageQuery 应支持 equals 和 hashCode")
    void sysRoleMenuPageQuery_shouldSupportEqualsAndHashCode() {
        SysRoleMenuPageQuery pageQuery1 = new SysRoleMenuPageQuery();
        pageQuery1.setId(1L);
        pageQuery1.setPageNum(1);
        pageQuery1.setPageSize(10);

        SysRoleMenuPageQuery pageQuery2 = new SysRoleMenuPageQuery();
        pageQuery2.setId(1L);
        pageQuery2.setPageNum(1);
        pageQuery2.setPageSize(10);

        assertThat(pageQuery1).isEqualTo(pageQuery2);
        assertThat(pageQuery1.hashCode()).isEqualTo(pageQuery2.hashCode());
    }

    @Test
    @DisplayName("SysRoleMenuView 应支持无参构造和 setter/getter")
    void sysRoleMenuView_shouldSupportGetterSetter() {
        SysRoleMenuView view = new SysRoleMenuView();
        view.setId(1L);
        view.setRoleId(2L);
        view.setMenuId(3L);
        view.setDeptId(4L);
        view.setDeleteStatus(false);
        view.setVersion(1);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getRoleId()).isEqualTo(2L);
        assertThat(view.getMenuId()).isEqualTo(3L);
        assertThat(view.getDeptId()).isEqualTo(4L);
        assertThat(view.getDeleteStatus()).isFalse();
        assertThat(view.getVersion()).isEqualTo(1);
    }
}
