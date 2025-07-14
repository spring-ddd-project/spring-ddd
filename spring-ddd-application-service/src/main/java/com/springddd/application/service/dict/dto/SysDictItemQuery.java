package com.springddd.application.service.dict.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@FieldNameConstants
public class SysDictItemQuery implements Serializable {

    private Long id;

    private Long dictId;

    private String itemLabel;

    private Integer itemValue;

    private Integer sortOrder;

    private Boolean itemStatus;

    private Boolean deleteStatus;
}
