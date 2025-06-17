package com.springddd.application.service.user.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysUserRoleCommand implements Serializable {

    private Long id;

    private Long userId;

    private Long roleId;

    private Long deptId;

    private String deleteStatus;

    private String version;
}
