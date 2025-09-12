package com.springddd.domain.menu;

public record AdvancedOptions(Integer order, String icon, Integer menuType, Boolean visible, Boolean menuStatus) {

    // Catalog
    public AdvancedOptions(Integer order, Integer menuType, String icon, Boolean MenuStatus, Boolean visible) {
        this(order, icon, menuType, visible, MenuStatus);
    }

    // Menu default

    // Button
    public AdvancedOptions(Integer order, Integer menuType, Boolean MenuStatus) {
        this(order, null, menuType, null, MenuStatus);
    }
}
