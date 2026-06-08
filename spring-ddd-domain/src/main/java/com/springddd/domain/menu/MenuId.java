package com.springddd.domain.menu;

import com.springddd.domain.AggregateRootId;

public record MenuId(Long value) implements AggregateRootId<Long> {
}
