package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictItemLabelNullException;
import org.springframework.util.ObjectUtils;

public record ItemLabel(String value) {

    public ItemLabel {
        if (ObjectUtils.isEmpty(value)) {
            throw new DictItemLabelNullException();
        }
    }
}
