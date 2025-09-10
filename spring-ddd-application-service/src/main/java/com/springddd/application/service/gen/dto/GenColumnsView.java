package com.springddd.application.service.gen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenColumnsView implements Serializable {

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

    public GenColumnsView(String propColumnKey, String propColumnName, String propColumnType, String propColumnComment) {
        this.propColumnKey = propColumnKey;
        this.propColumnName = propColumnName;
        this.propColumnType = propColumnType;
        this.propColumnComment = propColumnComment;
    }
}
