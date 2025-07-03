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

    private Byte propValueObject;

    private String propColumnKey;

    private String propColumnName;

    private String propColumnType;

    private String propColumnComment;

    private String propJavaEntity;

    private String propJavaType;

    private Long propDictId;

    private Byte tableVisible;

    private Byte tableOrder;

    private Byte tableFilter;

    private Integer tableFilterComponent;

    private Integer tableFilterType;

    private Integer formComponent;

    private Byte formVisible;

    private Byte formRequired;

    private Boolean deleteStatus;
}
