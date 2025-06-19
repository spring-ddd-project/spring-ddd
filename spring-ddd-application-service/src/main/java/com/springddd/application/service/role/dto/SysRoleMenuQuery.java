package com.springddd.application.service.role.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysRoleMenuQuery implements Serializable {

    private Long id;

    private Long roleId;

    private Long menuId;

    private Long deptId;

    private String deleteStatus;

    private String version;

    @NotNull(message = "pageNum can not be null")
    private Integer pageNum;

    @NotNull(message = "pageSize can not be null")
    private Integer pageSize;
}
