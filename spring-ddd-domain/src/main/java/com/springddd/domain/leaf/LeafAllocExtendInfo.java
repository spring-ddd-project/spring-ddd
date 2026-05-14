package com.springddd.domain.leaf;

public record LeafAllocExtendInfo(long maxId, int step) {
    public LeafAllocExtendInfo {
        if (step <= 0) {
            throw new IllegalArgumentException("step must be positive");
        }
    }
}
