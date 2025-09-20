package com.springddd.domain.menu;

public record Menu(String menuPath, String component, Boolean affixTab, Boolean noBasicLayout, Boolean embedded) {
}
