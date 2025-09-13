package com.springddd.domain.gen;

import com.springddd.domain.AggregateRootId;

public record InfoId(Long value) implements AggregateRootId<Long> {
}
