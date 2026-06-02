package com.springddd.domain.leaf.exception;

import com.springddd.domain.util.ErrorCode;

public class BizTagNullException extends LeafAllocException {

    public BizTagNullException() {
        super(ErrorCode.LEAF_BIZ_TAG_NULL);
    }
}
