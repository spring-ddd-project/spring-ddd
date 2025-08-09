package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.ColumnTypeNullException;
import org.springframework.util.ObjectUtils;

public record ColumnType(String value) {

    public ColumnType {
        if (ObjectUtils.isEmpty(value)) {
            throw new ColumnTypeNullException();
        }
    }
}
