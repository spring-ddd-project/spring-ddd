package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.ColumnNameNullException;
import org.springframework.util.ObjectUtils;

public record ColumnName(String value) {

    public ColumnName {
        if (ObjectUtils.isEmpty(value)) {
            throw new ColumnNameNullException();
        }
    }
}
