package com.springddd.domain.event;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
public class EventSourcingException extends DomainException {

    public EventSourcingException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
