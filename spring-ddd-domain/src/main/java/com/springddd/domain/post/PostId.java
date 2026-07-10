package com.springddd.domain.post;

import com.springddd.domain.AggregateRootId;

public record PostId(Long value) implements AggregateRootId<Long> {
}
