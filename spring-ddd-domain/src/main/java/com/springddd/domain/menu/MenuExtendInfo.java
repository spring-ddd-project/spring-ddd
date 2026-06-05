package com.springddd.domain.menu;

public record MenuExtendInfo(Integer order, String title, String icon, Integer menuType, Boolean visible, Boolean menuStatus) {

    // Catalog convenience constructor
    public MenuExtendInfo(Integer order, String title, Integer menuType, String icon, Boolean MenuStatus, Boolean visible) {
        this(order, title, icon, menuType, visible, MenuStatus);
    }

    // Button convenience constructor
    public MenuExtendInfo(Integer order, Integer menuType, Boolean menuStatus) {
        this(order, null, null, menuType, null, menuStatus);
    }

    // creatorId convenience constructor
    public MenuExtendInfo(Long creatorId) {
        this((Integer) null, (String) null, (String) null, (Integer) null, (Boolean) null, (Boolean) null);
    }
}
