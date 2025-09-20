package com.springddd.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GenColumnBindEntity Tests")
class GenColumnBindEntityTest {

    @Test
    @DisplayName("should create instance with default constructor")
    void create_WithDefaultConstructor_Success() {
        GenColumnBindEntity entity = new GenColumnBindEntity();
        assertThat(entity).isNotNull();
    }

    @Test
    @DisplayName("should set and get all fields correctly")
    void setterGetter_AllFields_Success() {
        GenColumnBindEntity entity = new GenColumnBindEntity();

        Long id = 1L;
        String columnType = "VARCHAR";
        String entityType = "String";
        Byte componentType = 1;
        Byte typescriptType = 2;
        Boolean deleteStatus = false;
        String createBy = "admin";
        LocalDateTime createTime = LocalDateTime.now();
        String updateBy = "admin";
        LocalDateTime updateTime = LocalDateTime.now();
        Integer version = 0;

        entity.setId(id);
        entity.setColumnType(columnType);
        entity.setEntityType(entityType);
        entity.setComponentType(componentType);
        entity.setTypescriptType(typescriptType);
        entity.setDeleteStatus(deleteStatus);
        entity.setCreateBy(createBy);
        entity.setCreateTime(createTime);
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(updateTime);
        entity.setVersion(version);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getColumnType()).isEqualTo(columnType);
        assertThat(entity.getEntityType()).isEqualTo(entityType);
        assertThat(entity.getComponentType()).isEqualTo(componentType);
        assertThat(entity.getTypescriptType()).isEqualTo(typescriptType);
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
        GenColumnBindEntity entity = new GenColumnBindEntity();

        entity.setId(null);
        entity.setColumnType(null);
        entity.setEntityType(null);
        entity.setComponentType(null);
        entity.setTypescriptType(null);
        entity.setDeleteStatus(null);
        entity.setCreateBy(null);
        entity.setCreateTime(null);
        entity.setUpdateBy(null);
        entity.setUpdateTime(null);
        entity.setVersion(null);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getColumnType()).isNull();
        assertThat(entity.getEntityType()).isNull();
        assertThat(entity.getComponentType()).isNull();
        assertThat(entity.getTypescriptType()).isNull();
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
        GenColumnBindEntity entity = new GenColumnBindEntity();
        entity.setId(1L);
        entity.setColumnType("VARCHAR");

        String str = entity.toString();
        assertThat(str).contains("1");
        assertThat(str).contains("VARCHAR");
    }
}