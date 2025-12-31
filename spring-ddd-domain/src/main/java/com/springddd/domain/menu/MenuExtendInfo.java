package com.springddd.domain.menu;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MenuExtendInfo {

    private Integer order;

    private String title;

    private Boolean affixTab;

    private Boolean noBasicLayout;

    private String icon;

    private Integer menuType;

    private Boolean visible;

    private Boolean embedded;

    private Boolean menuStatus;

    public MenuExtendInfo(Integer order, String title, Boolean affixTab, Boolean noBasicLayout, String icon, Integer menuType, Boolean visible, Boolean embedded, Boolean menuStatus) {
        this.order = order;
        this.title = title;
        this.affixTab = affixTab;
        this.noBasicLayout = noBasicLayout;
        this.icon = icon;
        this.menuType = menuType;
        this.visible = visible;
        this.embedded = embedded;
        this.menuStatus = menuStatus;
    }
}
