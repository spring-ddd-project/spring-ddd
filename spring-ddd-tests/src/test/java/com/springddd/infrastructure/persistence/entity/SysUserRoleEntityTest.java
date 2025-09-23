package com.springddd.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SysUserRoleEntity Tests")
class SysUserRoleEntityTest {

    @Test
    @DisplayName("should create instance with default constructor")
    void create_WithDefaultConstructor_Success() {
        SysUserRoleEntity entity = new SysUserRoleEntity();
        assertThat(entity).isNotNull();
    }

    @Test
    @DisplayName("should set and get all fields correctly")
    void setterGetter_AllFields_Success() {
        SysUserRoleEntity entity = new SysUserRoleEntity();

        Long id = 1L;
        Long userId = 100L;
        Long roleId = 200L;
        Long deptId = 1L;
        Boolean deleteStatus = false;
        String createBy = "admin";
        LocalDateTime createTime = LocalDateTime.now();
        String updateBy = "admin";
        LocalDateTime updateTime = LocalDateTime.now();
        Integer version = 0;

        entity.setId(id);
        entity.setUserId(userId);
        entity.setRoleId(roleId);
        entity.setDeptId(deptId);
        entity.setDeleteStatus(deleteStatus);
        entity.setCreateBy(createBy);
        entity.setCreateTime(createTime);
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(updateTime);
        entity.setVersion(version);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getUserId()).isEqualTo(userId);
        assertThat(entity.getRoleId()).isEqualTo(roleId);
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
        SysUserRoleEntity entity = new SysUserRoleEntity();

        entity.setId(null);
        entity.setUserId(null);
        entity.setRoleId(null);
        entity.setDeptId(null);
        entity.setDeleteStatus(null);
        entity.setCreateBy(null);
        entity.setCreateTime(null);
        entity.setUpdateBy(null);
        entity.setUpdateTime(null);
        entity.setVersion(null);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getUserId()).isNull();
        assertThat(entity.getRoleId()).isNull();
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
        SysUserRoleEntity entity = new SysUserRoleEntity();
        entity.setId(1L);
        entity.setUserId(100L);

        String str = entity.toString();
        assertThat(str).contains("1");
        assertThat(str).contains("100");
    }
}