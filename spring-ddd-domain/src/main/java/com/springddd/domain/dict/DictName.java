package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictNameNullException;
import org.springframework.util.ObjectUtils;

public record DictName(String value) {

    public DictName {
        if (ObjectUtils.isEmpty(value)) {
            throw new DictNameNullException();
        }
    }
}
