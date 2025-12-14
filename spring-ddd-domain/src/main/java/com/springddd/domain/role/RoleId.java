package com.springddd.domain.role;

import com.springddd.domain.AggregateRootId;

public record RoleId(Long value) implements AggregateRootId<Long> {
}
