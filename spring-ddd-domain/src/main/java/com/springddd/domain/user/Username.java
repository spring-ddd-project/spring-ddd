package com.springddd.domain.user;

import com.springddd.domain.user.exception.UsernameException;
import org.springframework.util.ObjectUtils;

public record Username(String value) {

    public Username {
        if (ObjectUtils.isEmpty(value)) {
            throw new UsernameException();
        }
    }
}
