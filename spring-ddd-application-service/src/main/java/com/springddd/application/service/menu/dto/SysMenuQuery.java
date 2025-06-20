package com.springddd.application.service.menu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysMenuQuery implements Serializable {

    private Long id;

    private Long parentId;

    private String name;

    private String path;

    private String component;

    private String redirect;

    private String permission;

    private Integer order;

    private String title;

    private Boolean affixTab;

    private Boolean noBasicLayout;

    private String icon;

    private Boolean menuType;

    private Boolean visible;

    private Boolean embedded;

    private Boolean menuStatus;

    private Long deptId;

    private Boolean deleteStatus;

    @NotNull(message = "pageNum can not be null")
    private Integer pageNum;

    @NotNull(message = "pageSize can not be null")
    private Integer pageSize;
}
