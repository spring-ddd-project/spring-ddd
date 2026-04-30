package com.springddd.application.service.leaf.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import java.io.Serializable;

@Data
@FieldNameConstants
public class LeafAllocQuery implements Serializable {

    private Integer step;

    private String bizTag;

    private Long maxId;

    private String description;

    private Long id;

}
