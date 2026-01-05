package com.springddd.application.service.dept.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SysDeptView implements Serializable {

    private Long id;

    private Long parentId;

    private String deptName;

    private Integer sortOrder;

    private Boolean deptStatus;

    private Boolean deleteStatus;

    private List<SysDeptView> children;
}
