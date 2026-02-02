package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.AggregateNullException;
import org.springframework.util.ObjectUtils;

public record PropAggregate(String value) {

    public PropAggregate {
        if (ObjectUtils.isEmpty(value)) {
//            throw new AggregateNullException();
        }
    }
}
