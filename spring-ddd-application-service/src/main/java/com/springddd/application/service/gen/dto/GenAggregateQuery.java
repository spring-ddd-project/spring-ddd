package com.springddd.application.service.gen.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@FieldNameConstants
public class GenAggregateQuery implements Serializable {

    private Long id;

    private Long infoId;

    private String objectName;

    private String objectValue;

    private Byte objectType;

    private Boolean hasCreated;
}
