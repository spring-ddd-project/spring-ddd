package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.ColumnNameNullException;
import org.springframework.util.ObjectUtils;

public record PropColumnName(String value) {

    public PropColumnName {
        if (ObjectUtils.isEmpty(value)) {
            throw new ColumnNameNullException();
        }
    }
}
