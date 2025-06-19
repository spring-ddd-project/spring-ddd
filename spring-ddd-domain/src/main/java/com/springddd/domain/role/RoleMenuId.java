package com.springddd.domain.role;

import com.springddd.domain.AggregateRootId;

public record RoleMenuId(Long value) implements AggregateRootId<Long> {
}
