package com.springddd.domain.menu;

import lombok.Data;

@Data
public class MenuExtendInfo {

    private Integer order;

    private String title;

    private Boolean affixTab;

    private Boolean noBasicLayout;

    private String icon;

    private Boolean menuType;

    private Boolean visible;

    private Boolean embedded;

    private Boolean menuStatus;
}
