package com.springddd.application.service.menu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysMenuQuery implements Serializable {

    private Long id;

    private Long parentId;

    private String name;

    private String permission;

    private String path;

    private String component;

    private String icon;

    private String visible;

    private Integer sortOrder;

    private String embedded;

    private String menuType;

    private String menuStatus;

    private Long deptId;

    private Boolean deleteStatus;

    @NotNull(message = "pageNum can not be null")
    private Integer pageNum;

    @NotNull(message = "pageSize can not be null")
    private Integer pageSize;
}
