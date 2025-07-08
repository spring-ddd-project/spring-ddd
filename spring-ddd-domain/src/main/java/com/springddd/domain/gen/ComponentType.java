package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.ComponentTypeNullException;
import org.springframework.util.ObjectUtils;

public record ComponentType(Integer value) {

    public ComponentType {
        if (ObjectUtils.isEmpty(value)) {
            throw new ComponentTypeNullException();
        }
    }
}
