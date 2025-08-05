package com.springddd.domain.user.exception;

import com.springddd.domain.DomainException;
import lombok.Getter;

@Getter
public class UsernameException extends DomainException {

    public UsernameException() {
        super(1001, "error.user.name.null");
    }
}
