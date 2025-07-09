package com.springddd.domain.dict;

import com.springddd.domain.AggregateRootId;

public record DictId(Long value) implements AggregateRootId<Long> {
}
