package com.springddd.domain.user;

import com.springddd.domain.AggregateRootId;

public record UserRoleId(Long value) implements AggregateRootId<Long> {
}
