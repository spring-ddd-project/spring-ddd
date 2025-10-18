package com.springddd.domain.menu;

public record MenuExtendInfo(Integer order, String title, String icon, Integer menuType, Boolean visible, Boolean menuStatus) {

    // Catalog
    public MenuExtendInfo(Integer order, String title, Integer menuType, String icon, Boolean MenuStatus, Boolean visible) {
        this(order, title, icon, menuType, visible, MenuStatus);
    }

    // Menu default

    // Button
    public MenuExtendInfo(Integer order, Integer menuType, Boolean MenuStatus) {
        this(order, null, null, menuType, null, MenuStatus);
    }
}
