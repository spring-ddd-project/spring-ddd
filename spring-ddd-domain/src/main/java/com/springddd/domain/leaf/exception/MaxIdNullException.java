package com.springddd.domain.leaf.exception;

import com.springddd.domain.util.ErrorCode;

public class MaxIdNullException extends LeafAllocException {

    public MaxIdNullException() {
        super(ErrorCode.LEAF_MAX_ID_NULL);
    }
}
