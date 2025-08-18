package com.springddd.application.service.role.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@FieldNameConstants
public class SysRoleQuery implements Serializable {

    private Long id;

    private String roleName;

    private String roleCode;

    private String roleDesc;

    private Integer dataScope;

    private Boolean roleStatus;

    private Boolean owner;

    private Long deptId;

    private Boolean deleteStatus;

    private Integer version;
}
