package com.springddd.application.service.gen.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@FieldNameConstants
public class GenProjectInfoQuery implements Serializable {

    private Long id;

    private String databaseName;

    private String tableName;

    private String packageName;

    private String className;

    private String requestName;

    private Boolean deleteStatus;
}
