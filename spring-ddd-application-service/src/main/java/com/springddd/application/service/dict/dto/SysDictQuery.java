package com.springddd.application.service.dict.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@FieldNameConstants
public class SysDictQuery implements Serializable {

    private Long id;

    private String dictName;

    private String dictCode;

    private Integer sortOrder;

    private Boolean dictStatus;

    private Boolean deleteStatus;
}
