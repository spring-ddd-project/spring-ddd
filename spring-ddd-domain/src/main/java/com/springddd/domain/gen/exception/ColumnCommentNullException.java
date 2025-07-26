package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class ColumnCommentNullException extends DomainException {

    public ColumnCommentNullException() {
        super(ErrorCode.GEN_INFO_COLUMN_COMMENT_NULL);
    }
}
