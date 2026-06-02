package com.springddd.domain.leaf;

public record Description(String value) {

    public Description {
        if (value == null) {
            value = "";
        }
    }
}
