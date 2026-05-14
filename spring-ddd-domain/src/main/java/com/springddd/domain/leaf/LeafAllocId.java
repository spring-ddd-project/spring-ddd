package com.springddd.domain.leaf;

import com.springddd.domain.AggregateRootId;

public record LeafAllocId(String value) implements AggregateRootId<String> {
}
