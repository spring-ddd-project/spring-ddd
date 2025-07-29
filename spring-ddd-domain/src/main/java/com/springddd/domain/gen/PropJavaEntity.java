package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.JavaEntityNullException;
import org.springframework.util.ObjectUtils;

public record PropJavaEntity(String value) {

    public PropJavaEntity {
        if (ObjectUtils.isEmpty(value)) {
            throw new JavaEntityNullException();
        }
    }
}
