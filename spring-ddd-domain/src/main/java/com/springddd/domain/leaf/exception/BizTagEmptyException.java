package com.springddd.domain.leaf.exception;

import com.springddd.domain.util.ErrorCode;

public class BizTagEmptyException extends LeafAllocException {

    public BizTagEmptyException() {
        super(ErrorCode.LEAF_BIZ_TAG_EMPTY);
    }
}
