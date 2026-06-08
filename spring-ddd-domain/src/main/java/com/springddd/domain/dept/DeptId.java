package com.springddd.domain.dept;

import com.springddd.domain.AggregateRootId;

public record DeptId(Long value) implements AggregateRootId<Long> {
}
