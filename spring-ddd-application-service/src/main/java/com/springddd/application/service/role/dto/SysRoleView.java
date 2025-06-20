package com.springddd.application.service.role.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysRoleView implements Serializable {

    private Long id;

    private String roleName;

    private String roleCode;

    private String roleDesc;

    private String dataScope;

    private String roleStatus;

    private Long deptId;

    private Boolean deleteStatus;

    private Integer version;
}
