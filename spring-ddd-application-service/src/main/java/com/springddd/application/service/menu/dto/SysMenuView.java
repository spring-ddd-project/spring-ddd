package com.springddd.application.service.menu.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SysMenuView implements Serializable {

    private Meta meta;

    private Long id;

    private Long parentId;

    private String name;

    private String path;

    private String component;

    private String redirect;

    private String permission;

    private Integer menuType;

    private Boolean visible;

    private Boolean embedded;

    private Boolean menuStatus;

    private Long deptId;

    private Boolean deleteStatus;

    private List<SysMenuView> children;

    @Data
    public static class Meta implements Serializable{

        private Integer order;

        private String title;

        private Boolean affixTab;

        private Boolean noBasicLayout;

        private String icon;
    }
}
