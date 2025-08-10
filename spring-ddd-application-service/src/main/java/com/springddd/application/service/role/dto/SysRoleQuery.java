package com.springddd.application.service.role.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysRoleQuery implements Serializable {

    private Long id;

    private String roleName;

    private String roleCode;

    private String roleDesc;

    private Integer dataScope;

    private Boolean roleStatus;

    private Long deptId;

    private Boolean deleteStatus;

    private Integer version;

    @NotNull(message = "pageNum can not be null")
    private Integer pageNum;

    @NotNull(message = "pageSize can not be null")
    private Integer pageSize;
}
