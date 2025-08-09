package com.springddd.application.service.gen.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@FieldNameConstants
public class GenColumnBindQuery implements Serializable {

    private Long id;

    private String columnType;

    private String entityType;

    private String componentName;

    private Boolean deleteStatus;
}
