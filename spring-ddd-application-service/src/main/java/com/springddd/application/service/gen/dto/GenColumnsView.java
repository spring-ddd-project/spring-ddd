package com.springddd.application.service.gen.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenColumnsView implements Serializable {

    private Long id;

    private Long infoId;

    private Boolean propValueObject;

    private Boolean propColumnKey;

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

    public GenColumnsView(Boolean propColumnKey, String propColumnName, String propColumnType, String propColumnComment) {
        this.propColumnKey = propColumnKey;
        this.propColumnName = propColumnName;
        this.propColumnType = propColumnType;
        this.propColumnComment = propColumnComment;
    }
}
