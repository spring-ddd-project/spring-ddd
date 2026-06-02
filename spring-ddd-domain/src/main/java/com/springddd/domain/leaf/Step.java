package com.springddd.domain.leaf;

import com.springddd.domain.leaf.exception.StepInvalidException;
import com.springddd.domain.leaf.exception.StepNullException;

public record Step(Integer value) {

    public Step {
        if (value == null) {
            throw new StepNullException();
        }
        if (value < 1) {
            throw new StepInvalidException();
        }
    }
}
