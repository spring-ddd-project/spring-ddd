package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.RequestNameNullException;
import org.springframework.util.ObjectUtils;

public record RequestName(String value) {

    public RequestName {
        if (ObjectUtils.isEmpty(value)) {
            throw new RequestNameNullException();
        }
    }
}
