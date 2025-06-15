package com.springddd.domain.user;

import com.springddd.domain.AggregateRootId;

public record UserId(Long value) implements AggregateRootId<Long> {

}