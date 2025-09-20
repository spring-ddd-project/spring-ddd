package com.springddd.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SysRoleMenuEntity Tests")
class SysRoleMenuEntityTest {

    @Test
    @DisplayName("should create instance with default constructor")
    void create_WithDefaultConstructor_Success() {
        SysRoleMenuEntity entity = new SysRoleMenuEntity();
        assertThat(entity).isNotNull();
    }

    @Test
    @DisplayName("should set and get all fields correctly")
    void setterGetter_AllFields_Success() {
        SysRoleMenuEntity entity = new SysRoleMenuEntity();

        Long id = 1L;
        Long roleId = 100L;
        Long menuId = 200L;
        Long deptId = 1L;
        Boolean deleteStatus = false;
        String createBy = "admin";
        LocalDateTime createTime = LocalDateTime.now();
        String updateBy = "admin";
        LocalDateTime updateTime = LocalDateTime.now();
        Integer version = 0;

        entity.setId(id);
        entity.setRoleId(roleId);
        entity.setMenuId(menuId);
        entity.setDeptId(deptId);
        entity.setDeleteStatus(deleteStatus);
        entity.setCreateBy(createBy);
        entity.setCreateTime(createTime);
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(updateTime);
        entity.setVersion(version);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getRoleId()).isEqualTo(roleId);
        assertThat(entity.getMenuId()).isEqualTo(menuId);
        assertThat(entity.getDeptId()).isEqualTo(deptId);
        assertThat(entity.getDeleteStatus()).isEqualTo(deleteStatus);
        assertThat(entity.getCreateBy()).isEqualTo(createBy);
        assertThat(entity.getCreateTime()).isEqualTo(createTime);
        assertThat(entity.getUpdateBy()).isEqualTo(updateBy);
        assertThat(entity.getUpdateTime()).isEqualTo(updateTime);
        assertThat(entity.getVersion()).isEqualTo(version);
    }

    @Test
    @DisplayName("should handle null values for optional fields")
    void setterGetter_NullValues_Success() {
        SysRoleMenuEntity entity = new SysRoleMenuEntity();

        entity.setId(null);
        entity.setRoleId(null);
        entity.setMenuId(null);
        entity.setDeptId(null);
        entity.setDeleteStatus(null);
        entity.setCreateBy(null);
        entity.setCreateTime(null);
        entity.setUpdateBy(null);
        entity.setUpdateTime(null);
        entity.setVersion(null);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getRoleId()).isNull();
        assertThat(entity.getMenuId()).isNull();
        assertThat(entity.getDeptId()).isNull();
        assertThat(entity.getDeleteStatus()).isNull();
        assertThat(entity.getCreateBy()).isNull();
        assertThat(entity.getCreateTime()).isNull();
        assertThat(entity.getUpdateBy()).isNull();
        assertThat(entity.getUpdateTime()).isNull();
        assertThat(entity.getVersion()).isNull();
    }

    @Test
    @DisplayName("toString should contain all field values")
    void toString_ShouldContainFieldValues() {
        SysRoleMenuEntity entity = new SysRoleMenuEntity();
        entity.setId(1L);
        entity.setRoleId(100L);

        String str = entity.toString();
        assertThat(str).contains("1");
        assertThat(str).contains("100");
    }
}