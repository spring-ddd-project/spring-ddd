package com.springddd.domain.user;

import com.springddd.domain.AggregateRootId;
import lombok.Data;

@Data
public class UserId implements AggregateRootId<Long> {

    private final Long value;

    public UserId(Long value) {
        this.value = value;
    }

    @Override
    public Long getValue() {
        return this.value;
    }
}
