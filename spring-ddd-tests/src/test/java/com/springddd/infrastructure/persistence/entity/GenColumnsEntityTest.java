package com.springddd.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GenColumnsEntity Tests")
class GenColumnsEntityTest {

    @Test
    @DisplayName("should create instance with default constructor")
    void create_WithDefaultConstructor_Success() {
        GenColumnsEntity entity = new GenColumnsEntity();
        assertThat(entity).isNotNull();
    }

    @Test
    @DisplayName("should set and get all fields correctly")
    void setterGetter_AllFields_Success() {
        GenColumnsEntity entity = new GenColumnsEntity();

        Long id = 1L;
        Long infoId = 100L;
        String propColumnKey = "id";
        String propColumnName = "ID";
        String propColumnType = "BIGINT";
        String propColumnComment = "Primary key";
        String propJavaEntity = "Long";
        String propJavaType = "Long";
        Long propDictId = 200L;
        Boolean tableVisible = true;
        Boolean tableOrder = false;
        Boolean tableFilter = true;
        Byte tableFilterComponent = 1;
        Byte tableFilterType = 2;
        Byte typescriptType = 1;
        Byte formComponent = 3;
        Boolean formVisible = true;
        Boolean formRequired = false;
        String en = "English label";
        String locale = "en_US";
        Boolean deleteStatus = false;
        String createBy = "admin";
        LocalDateTime createTime = LocalDateTime.now();
        String updateBy = "admin";
        LocalDateTime updateTime = LocalDateTime.now();
        Integer version = 0;

        entity.setId(id);
        entity.setInfoId(infoId);
        entity.setPropColumnKey(propColumnKey);
        entity.setPropColumnName(propColumnName);
        entity.setPropColumnType(propColumnType);
        entity.setPropColumnComment(propColumnComment);
        entity.setPropJavaEntity(propJavaEntity);
        entity.setPropJavaType(propJavaType);
        entity.setPropDictId(propDictId);
        entity.setTableVisible(tableVisible);
        entity.setTableOrder(tableOrder);
        entity.setTableFilter(tableFilter);
        entity.setTableFilterComponent(tableFilterComponent);
        entity.setTableFilterType(tableFilterType);
        entity.setTypescriptType(typescriptType);
        entity.setFormComponent(formComponent);
        entity.setFormVisible(formVisible);
        entity.setFormRequired(formRequired);
        entity.setEn(en);
        entity.setLocale(locale);
        entity.setDeleteStatus(deleteStatus);
        entity.setCreateBy(createBy);
        entity.setCreateTime(createTime);
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(updateTime);
        entity.setVersion(version);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getInfoId()).isEqualTo(infoId);
        assertThat(entity.getPropColumnKey()).isEqualTo(propColumnKey);
        assertThat(entity.getPropColumnName()).isEqualTo(propColumnName);
        assertThat(entity.getPropColumnType()).isEqualTo(propColumnType);
        assertThat(entity.getPropColumnComment()).isEqualTo(propColumnComment);
        assertThat(entity.getPropJavaEntity()).isEqualTo(propJavaEntity);
        assertThat(entity.getPropJavaType()).isEqualTo(propJavaType);
        assertThat(entity.getPropDictId()).isEqualTo(propDictId);
        assertThat(entity.getTableVisible()).isEqualTo(tableVisible);
        assertThat(entity.getTableOrder()).isEqualTo(tableOrder);
        assertThat(entity.getTableFilter()).isEqualTo(tableFilter);
        assertThat(entity.getTableFilterComponent()).isEqualTo(tableFilterComponent);
        assertThat(entity.getTableFilterType()).isEqualTo(tableFilterType);
        assertThat(entity.getTypescriptType()).isEqualTo(typescriptType);
        assertThat(entity.getFormComponent()).isEqualTo(formComponent);
        assertThat(entity.getFormVisible()).isEqualTo(formVisible);
        assertThat(entity.getFormRequired()).isEqualTo(formRequired);
        assertThat(entity.getEn()).isEqualTo(en);
        assertThat(entity.getLocale()).isEqualTo(locale);
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
        GenColumnsEntity entity = new GenColumnsEntity();

        entity.setId(null);
        entity.setInfoId(null);
        entity.setPropColumnKey(null);
        entity.setPropColumnName(null);
        entity.setPropColumnType(null);
        entity.setPropColumnComment(null);
        entity.setPropJavaEntity(null);
        entity.setPropJavaType(null);
        entity.setPropDictId(null);
        entity.setTableVisible(null);
        entity.setTableOrder(null);
        entity.setTableFilter(null);
        entity.setTableFilterComponent(null);
        entity.setTableFilterType(null);
        entity.setTypescriptType(null);
        entity.setFormComponent(null);
        entity.setFormVisible(null);
        entity.setFormRequired(null);
        entity.setEn(null);
        entity.setLocale(null);
        entity.setDeleteStatus(null);
        entity.setCreateBy(null);
        entity.setCreateTime(null);
        entity.setUpdateBy(null);
        entity.setUpdateTime(null);
        entity.setVersion(null);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getInfoId()).isNull();
        assertThat(entity.getPropColumnKey()).isNull();
        assertThat(entity.getPropColumnName()).isNull();
        assertThat(entity.getPropColumnType()).isNull();
        assertThat(entity.getPropColumnComment()).isNull();
        assertThat(entity.getPropJavaEntity()).isNull();
        assertThat(entity.getPropJavaType()).isNull();
        assertThat(entity.getPropDictId()).isNull();
        assertThat(entity.getTableVisible()).isNull();
        assertThat(entity.getTableOrder()).isNull();
        assertThat(entity.getTableFilter()).isNull();
        assertThat(entity.getTableFilterComponent()).isNull();
        assertThat(entity.getTableFilterType()).isNull();
        assertThat(entity.getTypescriptType()).isNull();
        assertThat(entity.getFormComponent()).isNull();
        assertThat(entity.getFormVisible()).isNull();
        assertThat(entity.getFormRequired()).isNull();
        assertThat(entity.getEn()).isNull();
        assertThat(entity.getLocale()).isNull();
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
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setId(1L);
        entity.setPropColumnKey("test_column");

        String str = entity.toString();
        assertThat(str).contains("1");
        assertThat(str).contains("test_column");
    }
}