package com.springddd.domain.leaf;

import com.springddd.domain.AggregateRootId;

public record LeafAllocId(Long value) implements AggregateRootId<Long> {
}
