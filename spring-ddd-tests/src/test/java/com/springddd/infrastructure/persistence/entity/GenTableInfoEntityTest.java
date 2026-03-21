package com.springddd.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GenTableInfoEntity Tests")
class GenTableInfoEntityTest {

    @Test
    @DisplayName("should create instance with default constructor")
    void create_WithDefaultConstructor_Success() {
        GenTableInfoEntity entity = new GenTableInfoEntity();
        assertThat(entity).isNotNull();
    }

    @Test
    @DisplayName("should set and get all fields correctly")
    void setterGetter_AllFields_Success() {
        GenTableInfoEntity entity = new GenTableInfoEntity();

        String tableName = "sys_user";
        String tableComment = "System user table";
        LocalDateTime createTime = LocalDateTime.now();
        String tableCollation = "utf8mb4_unicode_ci";

        entity.setTableName(tableName);
        entity.setTableComment(tableComment);
        entity.setCreateTime(createTime);
        entity.setTableCollation(tableCollation);

        assertThat(entity.getTableName()).isEqualTo(tableName);
        assertThat(entity.getTableComment()).isEqualTo(tableComment);
        assertThat(entity.getCreateTime()).isEqualTo(createTime);
        assertThat(entity.getTableCollation()).isEqualTo(tableCollation);
    }

    @Test
    @DisplayName("should handle null values for optional fields")
    void setterGetter_NullValues_Success() {
        GenTableInfoEntity entity = new GenTableInfoEntity();

        entity.setTableName(null);
        entity.setTableComment(null);
        entity.setCreateTime(null);
        entity.setTableCollation(null);

        assertThat(entity.getTableName()).isNull();
        assertThat(entity.getTableComment()).isNull();
        assertThat(entity.getCreateTime()).isNull();
        assertThat(entity.getTableCollation()).isNull();
    }

    @Test
    @DisplayName("toString should contain all field values")
    void toString_ShouldContainFieldValues() {
        GenTableInfoEntity entity = new GenTableInfoEntity();
        entity.setTableName("sys_user");
        entity.setTableComment("Test table");

        String str = entity.toString();
        assertThat(str).contains("sys_user");
        assertThat(str).contains("Test table");
    }
}