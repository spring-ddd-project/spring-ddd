package com.springddd.domain.leaf;

import com.springddd.domain.leaf.exception.MaxIdNullException;

public record MaxId(Long value) {

    public MaxId {
        if (value == null) {
            throw new MaxIdNullException();
        }
        if (value < 1) {
            value = 1L;
        }
    }
}
