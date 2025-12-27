package com.springddd.application.service.role.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysRoleMenuView implements Serializable {

    private Long id;

    private Long roleId;

    private Long menuId;

    private Long deptId;

    private Boolean deleteStatus;

    private Integer version;
}
