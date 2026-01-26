package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.ColumnKeyNullException;
import org.springframework.util.ObjectUtils;

public record PropColumnKey(String value) {

    public PropColumnKey {
        if (ObjectUtils.isEmpty(value)) {
            throw new ColumnKeyNullException();
        }
    }
}
