package com.springddd.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SysUserEntity Tests")
class SysUserEntityTest {

    @Test
    @DisplayName("should create instance with default constructor")
    void create_WithDefaultConstructor_Success() {
        SysUserEntity entity = new SysUserEntity();
        assertThat(entity).isNotNull();
    }

    @Test
    @DisplayName("should set and get all fields correctly")
    void setterGetter_AllFields_Success() {
        SysUserEntity entity = new SysUserEntity();

        Long id = 1L;
        String username = "admin";
        String password = "encrypted_password";
        String phone = "13800138000";
        String avatar = "avatar.png";
        String email = "admin@example.com";
        Boolean sex = true;
        Boolean lockStatus = false;
        Long deptId = 1L;
        Boolean deleteStatus = false;
        String createBy = "admin";
        LocalDateTime createTime = LocalDateTime.now();
        String updateBy = "admin";
        LocalDateTime updateTime = LocalDateTime.now();
        Integer version = 0;

        entity.setId(id);
        entity.setUsername(username);
        entity.setPassword(password);
        entity.setPhone(phone);
        entity.setAvatar(avatar);
        entity.setEmail(email);
        entity.setSex(sex);
        entity.setLockStatus(lockStatus);
        entity.setDeptId(deptId);
        entity.setDeleteStatus(deleteStatus);
        entity.setCreateBy(createBy);
        entity.setCreateTime(createTime);
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(updateTime);
        entity.setVersion(version);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getUsername()).isEqualTo(username);
        assertThat(entity.getPassword()).isEqualTo(password);
        assertThat(entity.getPhone()).isEqualTo(phone);
        assertThat(entity.getAvatar()).isEqualTo(avatar);
        assertThat(entity.getEmail()).isEqualTo(email);
        assertThat(entity.getSex()).isEqualTo(sex);
        assertThat(entity.getLockStatus()).isEqualTo(lockStatus);
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
        SysUserEntity entity = new SysUserEntity();

        entity.setId(null);
        entity.setUsername(null);
        entity.setPassword(null);
        entity.setPhone(null);
        entity.setAvatar(null);
        entity.setEmail(null);
        entity.setSex(null);
        entity.setLockStatus(null);
        entity.setDeptId(null);
        entity.setDeleteStatus(null);
        entity.setCreateBy(null);
        entity.setCreateTime(null);
        entity.setUpdateBy(null);
        entity.setUpdateTime(null);
        entity.setVersion(null);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getUsername()).isNull();
        assertThat(entity.getPassword()).isNull();
        assertThat(entity.getPhone()).isNull();
        assertThat(entity.getAvatar()).isNull();
        assertThat(entity.getEmail()).isNull();
        assertThat(entity.getSex()).isNull();
        assertThat(entity.getLockStatus()).isNull();
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
        SysUserEntity entity = new SysUserEntity();
        entity.setId(1L);
        entity.setUsername("testuser");

        String str = entity.toString();
        assertThat(str).contains("1");
        assertThat(str).contains("testuser");
    }
}