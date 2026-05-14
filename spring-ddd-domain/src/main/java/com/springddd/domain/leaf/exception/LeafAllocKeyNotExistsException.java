package com.springddd.domain.leaf.exception;

public class LeafAllocKeyNotExistsException extends RuntimeException {
    public LeafAllocKeyNotExistsException(String bizTag) {
        super("LeafAlloc key not exists: " + bizTag);
    }
}
