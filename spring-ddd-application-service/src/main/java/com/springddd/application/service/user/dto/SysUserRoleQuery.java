package com.springddd.application.service.user.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@FieldNameConstants
public class SysUserRoleQuery implements Serializable {

    private Long id;

    private Long userId;

    private Long roleId;

    private Long deptId;

    private String deleteStatus;

    private String version;
}
