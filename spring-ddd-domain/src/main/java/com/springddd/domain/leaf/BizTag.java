package com.springddd.domain.leaf;

import com.springddd.domain.leaf.exception.BizTagEmptyException;
import com.springddd.domain.leaf.exception.BizTagNullException;
import org.springframework.util.ObjectUtils;

public record BizTag(String value) {

    public BizTag {
        if (ObjectUtils.isEmpty(value)) {
            throw new BizTagNullException();
        }
        if (value.trim().isEmpty()) {
            throw new BizTagEmptyException();
        }
    }

    public String value() {
        return value.trim();
    }
}
