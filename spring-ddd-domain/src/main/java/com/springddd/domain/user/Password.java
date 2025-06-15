package com.springddd.domain.user;

import com.springddd.domain.user.exception.PasswordNullException;
import org.springframework.util.ObjectUtils;

public record Password(String value) {

    public Password {
        if (ObjectUtils.isEmpty(value)) {
            throw new PasswordNullException();
        }
    }
}
