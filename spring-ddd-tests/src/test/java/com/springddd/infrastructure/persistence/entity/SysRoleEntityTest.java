package com.springddd.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SysRoleEntity Tests")
class SysRoleEntityTest {

    @Test
    @DisplayName("should create instance with default constructor")
    void create_WithDefaultConstructor_Success() {
        SysRoleEntity entity = new SysRoleEntity();
        assertThat(entity).isNotNull();
    }

    @Test
    @DisplayName("should set and get all fields correctly")
    void setterGetter_AllFields_Success() {
        SysRoleEntity entity = new SysRoleEntity();

        Long id = 1L;
        String roleName = "Admin";
        String roleCode = "admin";
        String roleDesc = "Administrator role";
        Integer dataScope = 1;
        String dataPermission = "1=1";
        Boolean roleStatus = true;
        Boolean ownerStatus = true;
        Long deptId = 1L;
        Boolean deleteStatus = false;
        String createBy = "admin";
        LocalDateTime createTime = LocalDateTime.now();
        String updateBy = "admin";
        LocalDateTime updateTime = LocalDateTime.now();
        Integer version = 0;

        entity.setId(id);
        entity.setRoleName(roleName);
        entity.setRoleCode(roleCode);
        entity.setRoleDesc(roleDesc);
        entity.setDataScope(dataScope);
        entity.setDataPermission(dataPermission);
        entity.setRoleStatus(roleStatus);
        entity.setOwnerStatus(ownerStatus);
        entity.setDeptId(deptId);
        entity.setDeleteStatus(deleteStatus);
        entity.setCreateBy(createBy);
        entity.setCreateTime(createTime);
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(updateTime);
        entity.setVersion(version);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getRoleName()).isEqualTo(roleName);
        assertThat(entity.getRoleCode()).isEqualTo(roleCode);
        assertThat(entity.getRoleDesc()).isEqualTo(roleDesc);
        assertThat(entity.getDataScope()).isEqualTo(dataScope);
        assertThat(entity.getDataPermission()).isEqualTo(dataPermission);
        assertThat(entity.getRoleStatus()).isEqualTo(roleStatus);
        assertThat(entity.getOwnerStatus()).isEqualTo(ownerStatus);
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
        SysRoleEntity entity = new SysRoleEntity();

        entity.setId(null);
        entity.setRoleName(null);
        entity.setRoleCode(null);
        entity.setRoleDesc(null);
        entity.setDataScope(null);
        entity.setDataPermission(null);
        entity.setRoleStatus(null);
        entity.setOwnerStatus(null);
        entity.setDeptId(null);
        entity.setDeleteStatus(null);
        entity.setCreateBy(null);
        entity.setCreateTime(null);
        entity.setUpdateBy(null);
        entity.setUpdateTime(null);
        entity.setVersion(null);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getRoleName()).isNull();
        assertThat(entity.getRoleCode()).isNull();
        assertThat(entity.getRoleDesc()).isNull();
        assertThat(entity.getDataScope()).isNull();
        assertThat(entity.getDataPermission()).isNull();
        assertThat(entity.getRoleStatus()).isNull();
        assertThat(entity.getOwnerStatus()).isNull();
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
        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleName("TestRole");

        String str = entity.toString();
        assertThat(str).contains("1");
        assertThat(str).contains("TestRole");
    }
}