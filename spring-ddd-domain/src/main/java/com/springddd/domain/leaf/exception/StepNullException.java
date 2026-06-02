package com.springddd.domain.leaf.exception;

import com.springddd.domain.util.ErrorCode;

public class StepNullException extends LeafAllocException {

    public StepNullException() {
        super(ErrorCode.LEAF_STEP_NULL);
    }
}
