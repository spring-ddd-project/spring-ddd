package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.DatabaseNameNullException;
import org.springframework.util.ObjectUtils;

public record DatabaseName(String value) {

    public DatabaseName {
        if (ObjectUtils.isEmpty(value)) {
            throw new DatabaseNameNullException();
        }
    }
}
