package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.ValueObjectNullException;
import org.springframework.util.ObjectUtils;

public record PropValueObject(String value) {

    public PropValueObject {
        if (ObjectUtils.isEmpty(value)) {
            throw new ValueObjectNullException();
        }
    }
}
