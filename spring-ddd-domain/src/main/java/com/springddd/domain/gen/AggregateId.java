package com.springddd.domain.gen;

import com.springddd.domain.AggregateRootId;

public record AggregateId(Long value) implements AggregateRootId<Long> {
}
