package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.ClassNameNullException;
import org.springframework.util.ObjectUtils;

public record ClassName(String value) {

    public ClassName {
        if (ObjectUtils.isEmpty(value)) {
            throw new ClassNameNullException();
        }
    }
}
