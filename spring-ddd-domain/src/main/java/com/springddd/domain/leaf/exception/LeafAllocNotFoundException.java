package com.springddd.domain.leaf.exception;

import com.springddd.domain.util.ErrorCode;

public class LeafAllocNotFoundException extends LeafAllocException {

    public LeafAllocNotFoundException() {
        super(ErrorCode.LEAF_ALLOC_NOT_FOUND);
    }
}
