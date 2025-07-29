package com.springddd.application.service.gen.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@FieldNameConstants
public class GenInfoQuery implements Serializable {

    private Long id;

    private String tableName;

    private String packageName;

    private String className;

    private String requestName;

    private Boolean deleteStatus;
}
