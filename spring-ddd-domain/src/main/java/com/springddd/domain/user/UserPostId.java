package com.springddd.domain.user;

import com.springddd.domain.AggregateRootId;

public record UserPostId(Long value) implements AggregateRootId<Long> {
}
