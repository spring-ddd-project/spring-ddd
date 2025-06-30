package com.springddd.domain.gen;

import com.springddd.domain.AggregateRootId;

public record GenInfoId(Long value) implements AggregateRootId<Long> {
}
