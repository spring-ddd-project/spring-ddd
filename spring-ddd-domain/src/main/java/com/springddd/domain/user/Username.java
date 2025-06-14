package com.springddd.domain.user;

import lombok.Data;
import org.springframework.util.ObjectUtils;

@Data
public class Username {

    private final String value;

    public Username(String value) {
        if (ObjectUtils.isEmpty(value)) {

        }
        this.value = value;
    }
}
