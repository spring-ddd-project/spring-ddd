package com.springddd.application.service.leaf.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@FieldNameConstants
public class LeafAllocQuery implements Serializable {

    private Long id;

    private String bizTag;

    private Long maxId;

    private Integer step;

    private String description;

    private Boolean deleteStatus;
}
