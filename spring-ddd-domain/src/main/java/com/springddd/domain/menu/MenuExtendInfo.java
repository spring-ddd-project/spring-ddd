package com.springddd.domain.menu;

public record MenuExtendInfo(Integer order, String title, Boolean affixTab, Boolean noBasicLayout, String icon,
                             Integer menuType, Boolean visible, Boolean embedded, Boolean menuStatus) {

    // Catalog
    public MenuExtendInfo(Integer order, String title, Integer menuType, String icon, Boolean MenuStatus, Boolean visible) {
        this(order, title, null, null, icon, menuType, visible, null, MenuStatus);
    }

    // Menu default

    // Button
    public MenuExtendInfo(Integer order, Integer menuType, Boolean MenuStatus) {
        this(order, null, null, null, null, menuType, null, null, MenuStatus);
    }
}
