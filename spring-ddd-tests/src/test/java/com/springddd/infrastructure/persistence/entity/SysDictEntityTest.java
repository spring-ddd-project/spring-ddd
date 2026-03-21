package com.springddd.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SysDictEntity Tests")
class SysDictEntityTest {

    @Test
    @DisplayName("should create instance with default constructor")
    void create_WithDefaultConstructor_Success() {
        SysDictEntity entity = new SysDictEntity();
        assertThat(entity).isNotNull();
    }

    @Test
    @DisplayName("should set and get all fields correctly")
    void setterGetter_AllFields_Success() {
        SysDictEntity entity = new SysDictEntity();

        Long id = 1L;
        String dictName = "Status";
        String dictCode = "status";
        Integer sortOrder = 1;
        Boolean dictStatus = true;
        Boolean deleteStatus = false;
        String createBy = "admin";
        LocalDateTime createTime = LocalDateTime.now();
        String updateBy = "admin";
        LocalDateTime updateTime = LocalDateTime.now();
        Integer version = 0;

        entity.setId(id);
        entity.setDictName(dictName);
        entity.setDictCode(dictCode);
        entity.setSortOrder(sortOrder);
        entity.setDictStatus(dictStatus);
        entity.setDeleteStatus(deleteStatus);
        entity.setCreateBy(createBy);
        entity.setCreateTime(createTime);
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(updateTime);
        entity.setVersion(version);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getDictName()).isEqualTo(dictName);
        assertThat(entity.getDictCode()).isEqualTo(dictCode);
        assertThat(entity.getSortOrder()).isEqualTo(sortOrder);
        assertThat(entity.getDictStatus()).isEqualTo(dictStatus);
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
        SysDictEntity entity = new SysDictEntity();

        entity.setId(null);
        entity.setDictName(null);
        entity.setDictCode(null);
        entity.setSortOrder(null);
        entity.setDictStatus(null);
        entity.setDeleteStatus(null);
        entity.setCreateBy(null);
        entity.setCreateTime(null);
        entity.setUpdateBy(null);
        entity.setUpdateTime(null);
        entity.setVersion(null);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getDictName()).isNull();
        assertThat(entity.getDictCode()).isNull();
        assertThat(entity.getSortOrder()).isNull();
        assertThat(entity.getDictStatus()).isNull();
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
        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictName("TestDict");

        String str = entity.toString();
        assertThat(str).contains("1");
        assertThat(str).contains("TestDict");
    }
}