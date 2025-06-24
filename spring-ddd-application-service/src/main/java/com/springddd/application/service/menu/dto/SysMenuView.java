package com.springddd.application.service.menu.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysMenuView implements Serializable {

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

    private String deleteStatus;
}
