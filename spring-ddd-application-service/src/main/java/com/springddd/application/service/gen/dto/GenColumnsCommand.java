package com.springddd.application.service.gen.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenColumnsCommand implements Serializable {

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

    private Byte tableFilterComponent;

    private Byte tableFilterType;

    private Byte typescriptType;

    private Byte formComponent;

    private Boolean formVisible;

    private Boolean formRequired;
}
