package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.permission.DataPermissionEntity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GenTableInfoEntity {

    private String tableName;

    private String tableComment;

    private LocalDateTime createTime;

    private String tableCollation;
}
