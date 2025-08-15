package com.springddd.application.service.gen.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@FieldNameConstants
public class GenColumnsQuery implements Serializable {

    private Long id;

    private Long infoId;

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
