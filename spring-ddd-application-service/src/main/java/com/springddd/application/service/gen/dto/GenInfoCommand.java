package com.springddd.application.service.gen.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenInfoCommand implements Serializable {

    private Long id;

    private String tableName;

    private String packageName;

    private String className;

    private String requestName;

    private Boolean propValueObject;

    private String propColumnKey;

    private String propColumnName;

    private String propColumnType;

    private String propColumnComment;

    private String propJavaEntity;

    private String propJavaType;

    private Long propDictId;

    private Boolean tableVisible;

    private Boolean tableOrder;

    private Boolean tableFilter;

    private Integer tableFilterComponent;

    private Integer tableFilterType;

    private Integer formComponent;

    private Boolean formVisible;

    private Boolean formRequired;

    private Boolean deleteStatus;
}
