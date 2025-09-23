package com.springddd.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SysDeptEntity Tests")
class SysDeptEntityTest {

    @Test
    @DisplayName("should create instance with default constructor")
    void create_WithDefaultConstructor_Success() {
        SysDeptEntity entity = new SysDeptEntity();
        assertThat(entity).isNotNull();
    }

    @Test
    @DisplayName("should set and get all fields correctly")
    void setterGetter_AllFields_Success() {
        SysDeptEntity entity = new SysDeptEntity();

        Long id = 1L;
        Long parentId = 0L;
        String deptName = "Department";
        Integer sortOrder = 1;
        Boolean deptStatus = true;
        Boolean deleteStatus = false;
        String createBy = "admin";
        LocalDateTime createTime = LocalDateTime.now();
        String updateBy = "admin";
        LocalDateTime updateTime = LocalDateTime.now();
        Integer version = 0;

        entity.setId(id);
        entity.setParentId(parentId);
        entity.setDeptName(deptName);
        entity.setSortOrder(sortOrder);
        entity.setDeptStatus(deptStatus);
        entity.setDeleteStatus(deleteStatus);
        entity.setCreateBy(createBy);
        entity.setCreateTime(createTime);
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(updateTime);
        entity.setVersion(version);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getParentId()).isEqualTo(parentId);
        assertThat(entity.getDeptName()).isEqualTo(deptName);
        assertThat(entity.getSortOrder()).isEqualTo(sortOrder);
        assertThat(entity.getDeptStatus()).isEqualTo(deptStatus);
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
        SysDeptEntity entity = new SysDeptEntity();

        entity.setId(null);
        entity.setParentId(null);
        entity.setDeptName(null);
        entity.setSortOrder(null);
        entity.setDeptStatus(null);
        entity.setDeleteStatus(null);
        entity.setCreateBy(null);
        entity.setCreateTime(null);
        entity.setUpdateBy(null);
        entity.setUpdateTime(null);
        entity.setVersion(null);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getParentId()).isNull();
        assertThat(entity.getDeptName()).isNull();
        assertThat(entity.getSortOrder()).isNull();
        assertThat(entity.getDeptStatus()).isNull();
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
        SysDeptEntity entity = new SysDeptEntity();
        entity.setId(1L);
        entity.setDeptName("TestDept");

        String str = entity.toString();
        assertThat(str).contains("1");
        assertThat(str).contains("TestDept");
    }
}