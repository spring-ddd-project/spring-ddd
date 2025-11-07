package com.springddd.domain.leaf;

import com.springddd.domain.AggregateRootId;

public record LeafId(Long value) implements AggregateRootId<Long> {
}
