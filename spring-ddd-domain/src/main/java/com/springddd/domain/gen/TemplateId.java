package com.springddd.domain.gen;

import com.springddd.domain.AggregateRootId;

public record TemplateId(Long value) implements AggregateRootId<Long> {
}
