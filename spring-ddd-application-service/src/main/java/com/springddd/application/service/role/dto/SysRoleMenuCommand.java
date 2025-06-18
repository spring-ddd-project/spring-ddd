package com.springddd.application.service.role.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysRoleMenuCommand implements Serializable {

    private Long id;

    private Long roleId;

    private Long menuId;

    private Long deptId;

    private String deleteStatus;
}
