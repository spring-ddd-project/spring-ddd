package com.springddd.domain.user;

import com.springddd.domain.AggregateRootId;
import lombok.Data;

@Data
public class Password {

    private final String value;

    public Password(String value) {
        this.value = value;
    }
}
