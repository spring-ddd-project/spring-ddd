package com.springddd.application.service.gen.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@FieldNameConstants
public class GenTableInfoQuery implements Serializable {

    private String tableName;

    private String tableComment;

    private LocalDateTime createTime;

    private String tableCollation;
}
