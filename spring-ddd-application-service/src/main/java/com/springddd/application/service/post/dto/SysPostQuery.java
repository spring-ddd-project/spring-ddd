package com.springddd.application.service.post.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@FieldNameConstants
public class SysPostQuery implements Serializable {

    private Long id;

    private Long parentId;

    private String postCode;

    private String postName;

    private Integer sortOrder;

    private Boolean postStatus;

    private Boolean deleteStatus;

    private String createBy;
}
