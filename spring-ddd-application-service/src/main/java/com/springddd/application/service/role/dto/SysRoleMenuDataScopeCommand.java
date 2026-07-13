package com.springddd.application.service.role.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysRoleMenuDataScopeCommand implements Serializable {

    private Long id;

    private Long roleId;

    private Long menuId;

    private Integer dataScope;

    private Boolean deleteStatus;
}
