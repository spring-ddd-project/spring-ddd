package com.springddd.application.service.user.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysUserRoleView implements Serializable {

    private Long id;

    private Long userId;

    private Long roleId;

    private Long deptId;

    private Boolean deleteStatus;

    private Integer version;
}
