package com.springddd.domain.leaf.exception;

import com.springddd.domain.util.ErrorCode;

public class StepInvalidException extends LeafAllocException {

    public StepInvalidException() {
        super(ErrorCode.LEAF_STEP_INVALID);
    }
}
