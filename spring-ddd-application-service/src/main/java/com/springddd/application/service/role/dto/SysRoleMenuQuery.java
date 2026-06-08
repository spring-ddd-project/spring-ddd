package com.springddd.application.service.role.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@FieldNameConstants
public class SysRoleMenuQuery implements Serializable {

    private Long id;

    private Long roleId;

    private Long menuId;

    private Long deptId;

    private Boolean deleteStatus;

    private Integer version;
}
