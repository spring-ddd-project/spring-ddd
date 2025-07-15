package com.springddd.application.service.dept.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@FieldNameConstants
public class SysDeptQuery implements Serializable {

    private Long id;

    private Long parentId;

    private String deptName;

    private Integer sortOrder;

    private Boolean deptStatus;

    private Boolean deleteStatus;
}
