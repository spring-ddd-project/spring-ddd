package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.ComponentNameNullException;
import org.springframework.util.ObjectUtils;

public record ComponentName(String value) {

    public ComponentName {
        if (ObjectUtils.isEmpty(value)) {
            throw new ComponentNameNullException();
        }
    }
}
