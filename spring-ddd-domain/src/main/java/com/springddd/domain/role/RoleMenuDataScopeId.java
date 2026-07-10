package com.springddd.domain.role;

import com.springddd.domain.AggregateRootId;

public record RoleMenuDataScopeId(Long value) implements AggregateRootId<Long> {
}
