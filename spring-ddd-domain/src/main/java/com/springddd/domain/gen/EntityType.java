package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.JavaTypeNullException;
import org.springframework.util.ObjectUtils;

public record EntityType(String value) {

    public EntityType {
        if (ObjectUtils.isEmpty(value)) {
            throw new JavaTypeNullException();
        }
    }
}
