package com.springddd.domain.gen;

import com.springddd.domain.AggregateRootId;

public record ColumnBindId(Long value) implements AggregateRootId<Long> {
}
