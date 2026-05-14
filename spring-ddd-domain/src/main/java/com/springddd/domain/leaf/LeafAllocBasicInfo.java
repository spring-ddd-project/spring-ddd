package com.springddd.domain.leaf;

public record LeafAllocBasicInfo(String description) {
    public LeafAllocBasicInfo {
        if (description == null) {
            description = "";
        }
    }
}
