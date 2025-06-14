package com.springddd.domain.user;

import com.springddd.domain.exception.UsernameException;
import lombok.Data;
import org.springframework.util.ObjectUtils;

@Data
public class Username {

    private final String value;

    public Username(String value) {
        if (ObjectUtils.isEmpty(value)) {
            throw new UsernameException();
        }
        this.value = value;
    }
}
