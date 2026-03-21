package com.springddd.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GenAggregateEntity Tests")
class GenAggregateEntityTest {

    @Test
    @DisplayName("should create instance with default constructor")
    void create_WithDefaultConstructor_Success() {
        GenAggregateEntity entity = new GenAggregateEntity();
        assertThat(entity).isNotNull();
    }

    @Test
    @DisplayName("should set and get all fields correctly")
    void setterGetter_AllFields_Success() {
        GenAggregateEntity entity = new GenAggregateEntity();

        Long id = 1L;
        Long infoId = 100L;
        String objectName = "User";
        String objectValue = "user";
        Byte objectType = 1;
        Boolean hasCreated = false;
        String createBy = "admin";
        LocalDateTime createTime = LocalDateTime.now();
        String updateBy = "admin";
        LocalDateTime updateTime = LocalDateTime.now();
        Integer version = 0;

        entity.setId(id);
        entity.setInfoId(infoId);
        entity.setObjectName(objectName);
        entity.setObjectValue(objectValue);
        entity.setObjectType(objectType);
        entity.setHasCreated(hasCreated);
        entity.setCreateBy(createBy);
        entity.setCreateTime(createTime);
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(updateTime);
        entity.setVersion(version);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getInfoId()).isEqualTo(infoId);
        assertThat(entity.getObjectName()).isEqualTo(objectName);
        assertThat(entity.getObjectValue()).isEqualTo(objectValue);
        assertThat(entity.getObjectType()).isEqualTo(objectType);
        assertThat(entity.getHasCreated()).isEqualTo(hasCreated);
        assertThat(entity.getCreateBy()).isEqualTo(createBy);
        assertThat(entity.getCreateTime()).isEqualTo(createTime);
        assertThat(entity.getUpdateBy()).isEqualTo(updateBy);
        assertThat(entity.getUpdateTime()).isEqualTo(updateTime);
        assertThat(entity.getVersion()).isEqualTo(version);
    }

    @Test
    @DisplayName("should handle null values for optional fields")
    void setterGetter_NullValues_Success() {
        GenAggregateEntity entity = new GenAggregateEntity();

        entity.setId(null);
        entity.setInfoId(null);
        entity.setObjectName(null);
        entity.setObjectValue(null);
        entity.setObjectType(null);
        entity.setHasCreated(null);
        entity.setCreateBy(null);
        entity.setCreateTime(null);
        entity.setUpdateBy(null);
        entity.setUpdateTime(null);
        entity.setVersion(null);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getInfoId()).isNull();
        assertThat(entity.getObjectName()).isNull();
        assertThat(entity.getObjectValue()).isNull();
        assertThat(entity.getObjectType()).isNull();
        assertThat(entity.getHasCreated()).isNull();
        assertThat(entity.getCreateBy()).isNull();
        assertThat(entity.getCreateTime()).isNull();
        assertThat(entity.getUpdateBy()).isNull();
        assertThat(entity.getUpdateTime()).isNull();
        assertThat(entity.getVersion()).isNull();
    }

    @Test
    @DisplayName("toString should contain all field values")
    void toString_ShouldContainFieldValues() {
        GenAggregateEntity entity = new GenAggregateEntity();
        entity.setId(1L);
        entity.setObjectName("TestAggregate");

        String str = entity.toString();
        assertThat(str).contains("1");
        assertThat(str).contains("TestAggregate");
    }
}