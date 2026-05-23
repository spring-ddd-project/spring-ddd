package com.springddd.application.service.user.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoTest {

    @Test
    @DisplayName("SysUserCommand 应支持无参构造和 setter/getter")
    void sysUserCommand_shouldSupportGetterSetter() {
        SysUserCommand command = new SysUserCommand();
        command.setId(1L);
        command.setUsername("testuser");
        command.setPassword("password123");
        command.setPhone("13800138000");
        command.setAvatar("http://avatar.url");
        command.setEmail("test@example.com");
        command.setSex(true);
        command.setLockStatus(false);
        command.setDeptId(2L);
        command.setDeleteStatus(false);
        command.setVersion(1);

        assertThat(command.getId()).isEqualTo(1L);
        assertThat(command.getUsername()).isEqualTo("testuser");
        assertThat(command.getPassword()).isEqualTo("password123");
        assertThat(command.getPhone()).isEqualTo("13800138000");
        assertThat(command.getAvatar()).isEqualTo("http://avatar.url");
        assertThat(command.getEmail()).isEqualTo("test@example.com");
        assertThat(command.getSex()).isTrue();
        assertThat(command.getLockStatus()).isFalse();
        assertThat(command.getDeptId()).isEqualTo(2L);
        assertThat(command.getDeleteStatus()).isFalse();
        assertThat(command.getVersion()).isEqualTo(1);
    }

    @Test
    @DisplayName("SysUserCommand 初始值应为 null")
    void sysUserCommand_shouldHaveNullInitialValues() {
        SysUserCommand command = new SysUserCommand();

        assertThat(command.getId()).isNull();
        assertThat(command.getUsername()).isNull();
        assertThat(command.getPassword()).isNull();
        assertThat(command.getPhone()).isNull();
        assertThat(command.getAvatar()).isNull();
        assertThat(command.getEmail()).isNull();
        assertThat(command.getSex()).isNull();
        assertThat(command.getLockStatus()).isNull();
        assertThat(command.getDeptId()).isNull();
        assertThat(command.getDeleteStatus()).isNull();
        assertThat(command.getVersion()).isNull();
    }

    @Test
    @DisplayName("SysUserQuery 应支持无参构造和 setter/getter")
    void sysUserQuery_shouldSupportGetterSetter() {
        SysUserQuery query = new SysUserQuery();
        query.setId(1L);
        query.setUsername("testuser");
        query.setPassword("password123");
        query.setPhone("13800138000");
        query.setAvatar("http://avatar.url");
        query.setEmail("test@example.com");
        query.setSex(true);
        query.setLockStatus(false);
        query.setDeptId(2L);
        query.setDeleteStatus(false);
        query.setVersion(1);

        assertThat(query.getId()).isEqualTo(1L);
        assertThat(query.getUsername()).isEqualTo("testuser");
        assertThat(query.getPassword()).isEqualTo("password123");
        assertThat(query.getPhone()).isEqualTo("13800138000");
        assertThat(query.getAvatar()).isEqualTo("http://avatar.url");
        assertThat(query.getEmail()).isEqualTo("test@example.com");
        assertThat(query.getSex()).isTrue();
        assertThat(query.getLockStatus()).isFalse();
        assertThat(query.getDeptId()).isEqualTo(2L);
        assertThat(query.getDeleteStatus()).isFalse();
        assertThat(query.getVersion()).isEqualTo(1);
    }

    @Test
    @DisplayName("SysUserQuery 应支持 FieldNameConstants")
    void sysUserQuery_shouldSupportFieldNameConstants() {
        assertThat(SysUserQuery.Fields.id).isEqualTo("id");
        assertThat(SysUserQuery.Fields.username).isEqualTo("username");
        assertThat(SysUserQuery.Fields.password).isEqualTo("password");
        assertThat(SysUserQuery.Fields.phone).isEqualTo("phone");
        assertThat(SysUserQuery.Fields.avatar).isEqualTo("avatar");
        assertThat(SysUserQuery.Fields.email).isEqualTo("email");
        assertThat(SysUserQuery.Fields.sex).isEqualTo("sex");
        assertThat(SysUserQuery.Fields.lockStatus).isEqualTo("lockStatus");
        assertThat(SysUserQuery.Fields.deptId).isEqualTo("deptId");
        assertThat(SysUserQuery.Fields.deleteStatus).isEqualTo("deleteStatus");
        assertThat(SysUserQuery.Fields.version).isEqualTo("version");
    }

    @Test
    @DisplayName("SysUserPageQuery 应支持无参构造和 setter/getter")
    void sysUserPageQuery_shouldSupportGetterSetter() {
        SysUserPageQuery pageQuery = new SysUserPageQuery();
        pageQuery.setId(1L);
        pageQuery.setUsername("testuser");
        pageQuery.setPassword("password123");
        pageQuery.setPhone("13800138000");
        pageQuery.setAvatar("http://avatar.url");
        pageQuery.setEmail("test@example.com");
        pageQuery.setSex(true);
        pageQuery.setLockStatus(false);
        pageQuery.setDeptId(2L);
        pageQuery.setDeleteStatus(false);
        pageQuery.setVersion(1);
        pageQuery.setPageNum(1);
        pageQuery.setPageSize(10);

        assertThat(pageQuery.getId()).isEqualTo(1L);
        assertThat(pageQuery.getUsername()).isEqualTo("testuser");
        assertThat(pageQuery.getPassword()).isEqualTo("password123");
        assertThat(pageQuery.getPhone()).isEqualTo("13800138000");
        assertThat(pageQuery.getAvatar()).isEqualTo("http://avatar.url");
        assertThat(pageQuery.getEmail()).isEqualTo("test@example.com");
        assertThat(pageQuery.getSex()).isTrue();
        assertThat(pageQuery.getLockStatus()).isFalse();
        assertThat(pageQuery.getDeptId()).isEqualTo(2L);
        assertThat(pageQuery.getDeleteStatus()).isFalse();
        assertThat(pageQuery.getVersion()).isEqualTo(1);
        assertThat(pageQuery.getPageNum()).isEqualTo(1);
        assertThat(pageQuery.getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("SysUserPageQuery 应支持 equals 和 hashCode")
    void sysUserPageQuery_shouldSupportEqualsAndHashCode() {
        SysUserPageQuery pageQuery1 = new SysUserPageQuery();
        pageQuery1.setId(1L);
        pageQuery1.setPageNum(1);
        pageQuery1.setPageSize(10);

        SysUserPageQuery pageQuery2 = new SysUserPageQuery();
        pageQuery2.setId(1L);
        pageQuery2.setPageNum(1);
        pageQuery2.setPageSize(10);

        assertThat(pageQuery1).isEqualTo(pageQuery2);
        assertThat(pageQuery1.hashCode()).isEqualTo(pageQuery2.hashCode());
    }

    @Test
    @DisplayName("SysUserView 应支持无参构造和 setter/getter")
    void sysUserView_shouldSupportGetterSetter() {
        SysUserView view = new SysUserView();
        view.setId(1L);
        view.setUsername("testuser");
        view.setPassword("password123");
        view.setPhone("13800138000");
        view.setAvatar("http://avatar.url");
        view.setEmail("test@example.com");
        view.setSex(true);
        view.setLockStatus(false);
        view.setDeptId(2L);
        view.setDeleteStatus(false);
        view.setVersion(1);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getUsername()).isEqualTo("testuser");
        assertThat(view.getPassword()).isEqualTo("password123");
        assertThat(view.getPhone()).isEqualTo("13800138000");
        assertThat(view.getAvatar()).isEqualTo("http://avatar.url");
        assertThat(view.getEmail()).isEqualTo("test@example.com");
        assertThat(view.getSex()).isTrue();
        assertThat(view.getLockStatus()).isFalse();
        assertThat(view.getDeptId()).isEqualTo(2L);
        assertThat(view.getDeleteStatus()).isFalse();
        assertThat(view.getVersion()).isEqualTo(1);
    }

    @Test
    @DisplayName("SysUserRoleCommand 应支持无参构造和 setter/getter")
    void sysUserRoleCommand_shouldSupportGetterSetter() {
        SysUserRoleCommand command = new SysUserRoleCommand();
        command.setId(1L);
        command.setUserId(2L);
        command.setRoleId(3L);
        command.setDeptId(4L);
        command.setDeleteStatus(false);
        command.setVersion(1);

        assertThat(command.getId()).isEqualTo(1L);
        assertThat(command.getUserId()).isEqualTo(2L);
        assertThat(command.getRoleId()).isEqualTo(3L);
        assertThat(command.getDeptId()).isEqualTo(4L);
        assertThat(command.getDeleteStatus()).isFalse();
        assertThat(command.getVersion()).isEqualTo(1);
    }

    @Test
    @DisplayName("SysUserRoleQuery 应支持无参构造和 setter/getter")
    void sysUserRoleQuery_shouldSupportGetterSetter() {
        SysUserRoleQuery query = new SysUserRoleQuery();
        query.setId(1L);
        query.setUserId(2L);
        query.setRoleId(3L);
        query.setDeptId(4L);
        query.setDeleteStatus("false");
        query.setVersion("1");

        assertThat(query.getId()).isEqualTo(1L);
        assertThat(query.getUserId()).isEqualTo(2L);
        assertThat(query.getRoleId()).isEqualTo(3L);
        assertThat(query.getDeptId()).isEqualTo(4L);
        assertThat(query.getDeleteStatus()).isEqualTo("false");
        assertThat(query.getVersion()).isEqualTo("1");
    }

    @Test
    @DisplayName("SysUserRoleQuery 应支持 FieldNameConstants")
    void sysUserRoleQuery_shouldSupportFieldNameConstants() {
        assertThat(SysUserRoleQuery.Fields.id).isEqualTo("id");
        assertThat(SysUserRoleQuery.Fields.userId).isEqualTo("userId");
        assertThat(SysUserRoleQuery.Fields.roleId).isEqualTo("roleId");
        assertThat(SysUserRoleQuery.Fields.deptId).isEqualTo("deptId");
        assertThat(SysUserRoleQuery.Fields.deleteStatus).isEqualTo("deleteStatus");
        assertThat(SysUserRoleQuery.Fields.version).isEqualTo("version");
    }

    @Test
    @DisplayName("SysUserRolePageQuery 应支持无参构造和 setter/getter")
    void sysUserRolePageQuery_shouldSupportGetterSetter() {
        SysUserRolePageQuery pageQuery = new SysUserRolePageQuery();
        pageQuery.setId(1L);
        pageQuery.setUserId(2L);
        pageQuery.setRoleId(3L);
        pageQuery.setDeptId(4L);
        pageQuery.setDeleteStatus("false");
        pageQuery.setVersion("1");
        pageQuery.setPageNum(1);
        pageQuery.setPageSize(10);

        assertThat(pageQuery.getId()).isEqualTo(1L);
        assertThat(pageQuery.getUserId()).isEqualTo(2L);
        assertThat(pageQuery.getRoleId()).isEqualTo(3L);
        assertThat(pageQuery.getDeptId()).isEqualTo(4L);
        assertThat(pageQuery.getDeleteStatus()).isEqualTo("false");
        assertThat(pageQuery.getVersion()).isEqualTo("1");
        assertThat(pageQuery.getPageNum()).isEqualTo(1);
        assertThat(pageQuery.getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("SysUserRolePageQuery 应支持 equals 和 hashCode")
    void sysUserRolePageQuery_shouldSupportEqualsAndHashCode() {
        SysUserRolePageQuery pageQuery1 = new SysUserRolePageQuery();
        pageQuery1.setId(1L);
        pageQuery1.setPageNum(1);
        pageQuery1.setPageSize(10);

        SysUserRolePageQuery pageQuery2 = new SysUserRolePageQuery();
        pageQuery2.setId(1L);
        pageQuery2.setPageNum(1);
        pageQuery2.setPageSize(10);

        assertThat(pageQuery1).isEqualTo(pageQuery2);
        assertThat(pageQuery1.hashCode()).isEqualTo(pageQuery2.hashCode());
    }

    @Test
    @DisplayName("SysUserRoleView 应支持无参构造和 setter/getter")
    void sysUserRoleView_shouldSupportGetterSetter() {
        SysUserRoleView view = new SysUserRoleView();
        view.setId(1L);
        view.setUserId(2L);
        view.setRoleId(3L);
        view.setDeptId(4L);
        view.setDeleteStatus(false);
        view.setVersion(1);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getUserId()).isEqualTo(2L);
        assertThat(view.getRoleId()).isEqualTo(3L);
        assertThat(view.getDeptId()).isEqualTo(4L);
        assertThat(view.getDeleteStatus()).isFalse();
        assertThat(view.getVersion()).isEqualTo(1);
    }
}
