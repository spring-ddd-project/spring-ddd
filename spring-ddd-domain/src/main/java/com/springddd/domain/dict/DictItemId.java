package com.springddd.domain.dict;

import com.springddd.domain.AggregateRootId;

public record DictItemId(Long value) implements AggregateRootId<Long> {
}
