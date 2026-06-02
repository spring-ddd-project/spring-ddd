package com.springddd.application.service.leaf.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@FieldNameConstants
public class LeafAllocQuery implements Serializable {

    private String bizTag;

    private Boolean deleteStatus;
}
