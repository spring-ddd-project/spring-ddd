package com.springddd.domain.menu;

public record MenuExtendInfo(Integer order, String title, String icon, Integer menuType, Boolean visible, Boolean menuStatus) {

    // Button convenience constructor
    public MenuExtendInfo(Integer order, Integer menuType, Boolean menuStatus) {
        this(order, null, null, menuType, null, menuStatus);
    }

    // creatorId convenience constructor
    public MenuExtendInfo(Long creatorId) {
        this(null, null, null, null, null, null);
    }
}
