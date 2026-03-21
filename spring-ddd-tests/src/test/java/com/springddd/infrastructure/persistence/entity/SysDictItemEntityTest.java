package com.springddd.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SysDictItemEntity Tests")
class SysDictItemEntityTest {

    @Test
    @DisplayName("should create instance with default constructor")
    void create_WithDefaultConstructor_Success() {
        SysDictItemEntity entity = new SysDictItemEntity();
        assertThat(entity).isNotNull();
    }

    @Test
    @DisplayName("should set and get all fields correctly")
    void setterGetter_AllFields_Success() {
        SysDictItemEntity entity = new SysDictItemEntity();

        Long id = 1L;
        Long dictId = 100L;
        String itemLabel = "Active";
        Integer itemValue = 1;
        Integer sortOrder = 1;
        Boolean itemStatus = true;
        Boolean deleteStatus = false;
        String createBy = "admin";
        LocalDateTime createTime = LocalDateTime.now();
        String updateBy = "admin";
        LocalDateTime updateTime = LocalDateTime.now();
        Integer version = 0;

        entity.setId(id);
        entity.setDictId(dictId);
        entity.setItemLabel(itemLabel);
        entity.setItemValue(itemValue);
        entity.setSortOrder(sortOrder);
        entity.setItemStatus(itemStatus);
        entity.setDeleteStatus(deleteStatus);
        entity.setCreateBy(createBy);
        entity.setCreateTime(createTime);
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(updateTime);
        entity.setVersion(version);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getDictId()).isEqualTo(dictId);
        assertThat(entity.getItemLabel()).isEqualTo(itemLabel);
        assertThat(entity.getItemValue()).isEqualTo(itemValue);
        assertThat(entity.getSortOrder()).isEqualTo(sortOrder);
        assertThat(entity.getItemStatus()).isEqualTo(itemStatus);
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
        SysDictItemEntity entity = new SysDictItemEntity();

        entity.setId(null);
        entity.setDictId(null);
        entity.setItemLabel(null);
        entity.setItemValue(null);
        entity.setSortOrder(null);
        entity.setItemStatus(null);
        entity.setDeleteStatus(null);
        entity.setCreateBy(null);
        entity.setCreateTime(null);
        entity.setUpdateBy(null);
        entity.setUpdateTime(null);
        entity.setVersion(null);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getDictId()).isNull();
        assertThat(entity.getItemLabel()).isNull();
        assertThat(entity.getItemValue()).isNull();
        assertThat(entity.getSortOrder()).isNull();
        assertThat(entity.getItemStatus()).isNull();
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
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        entity.setItemLabel("TestItem");

        String str = entity.toString();
        assertThat(str).contains("1");
        assertThat(str).contains("TestItem");
    }
}