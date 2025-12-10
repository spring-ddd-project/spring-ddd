package com.springddd.domain.user;

import com.springddd.domain.AggregateRootId;
import com.springddd.domain.exception.PasswordNullException;
import lombok.Data;
import org.springframework.util.ObjectUtils;

@Data
public class Password {

    private final String value;

    public Password(String value) {
        if (ObjectUtils.isEmpty(value)) {
            throw new PasswordNullException();
        }
        this.value = value;
    }
}
