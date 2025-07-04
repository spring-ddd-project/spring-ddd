package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.ColumnTypeNullException;
import org.springframework.util.ObjectUtils;

public record PropColumnType(String value) {

    public PropColumnType {
        if (ObjectUtils.isEmpty(value)) {
            throw new ColumnTypeNullException();
        }
    }
}
