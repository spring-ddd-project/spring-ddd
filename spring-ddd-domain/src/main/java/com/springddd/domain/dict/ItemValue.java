package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictItemValueNullException;
import org.springframework.util.ObjectUtils;

public record ItemValue(Integer value) {

    public ItemValue {
        if (ObjectUtils.isEmpty(value)) {
            throw new DictItemValueNullException();
        }
    }
}
