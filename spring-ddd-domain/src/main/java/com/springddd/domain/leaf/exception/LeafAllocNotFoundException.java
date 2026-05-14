package com.springddd.domain.leaf.exception;

public class LeafAllocNotFoundException extends RuntimeException {
    public LeafAllocNotFoundException(String bizTag) {
        super("LeafAlloc not found for bizTag: " + bizTag);
    }
}
