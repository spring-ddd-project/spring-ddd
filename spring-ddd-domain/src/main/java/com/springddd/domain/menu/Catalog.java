package com.springddd.domain.menu;

public record Catalog(String routePath, String component, String redirect) {

    public Catalog(String redirect) {
        this(null, null, redirect);
    }
}
