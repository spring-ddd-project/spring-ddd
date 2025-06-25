package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictCodeNullException;
import org.springframework.util.ObjectUtils;

public record DictCode(String value) {

    public DictCode {
        if (ObjectUtils.isEmpty(value)) {
            throw new DictCodeNullException();
        }
    }
}
