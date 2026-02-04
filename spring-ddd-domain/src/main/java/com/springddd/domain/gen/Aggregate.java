package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.AggregateNullException;
import org.springframework.util.ObjectUtils;

public record Aggregate(String value) {

    public Aggregate {
        if (ObjectUtils.isEmpty(value)) {
            throw new AggregateNullException();
        }
    }
}
