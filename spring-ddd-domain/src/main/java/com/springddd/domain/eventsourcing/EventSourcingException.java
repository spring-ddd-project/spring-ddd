package com.springddd.domain.eventsourcing;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

/**
 * 事件溯源相关异常。
 */
public class EventSourcingException extends DomainException {

    public EventSourcingException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
