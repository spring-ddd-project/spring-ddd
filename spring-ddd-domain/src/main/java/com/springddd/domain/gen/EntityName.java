package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.JavaEntityNullException;
import org.springframework.util.ObjectUtils;

public record EntityName(String value) {

    public EntityName {
        if (ObjectUtils.isEmpty(value)) {
            throw new JavaEntityNullException();
        }
    }
}
