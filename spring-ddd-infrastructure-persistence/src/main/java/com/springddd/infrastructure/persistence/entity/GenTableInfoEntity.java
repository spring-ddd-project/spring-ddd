package com.springddd.infrastructure.persistence.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GenTableInfoEntity {

    private String tableName;

    private String tableComment;

    private LocalDateTime createTime;

    private String tableCollation;
}
