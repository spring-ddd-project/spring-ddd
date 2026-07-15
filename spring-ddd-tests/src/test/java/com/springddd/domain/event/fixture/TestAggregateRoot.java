package com.springddd.domain.event.fixture;

import com.springddd.domain.AggregateRootId;
import com.springddd.domain.event.EventSourcingAggregateRoot;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestAggregateRoot extends EventSourcingAggregateRoot {

    private String entityId;
    private String name;
    private Integer count;

    public void apply(TestCreated event) {
        this.entityId = event.getEntityId();
        this.name = event.getName();
        this.count = 0;
        registerDomainEvent(event);
    }

    public void apply(TestCountChanged event) {
        this.count = event.getCount();
        registerDomainEvent(event);
    }

    public void changeCount(int count) {
        registerDomainEvent(TestCountChanged.of(this.entityId, count));
    }

    public record TestId(String value) implements AggregateRootId<String> {
    }
}
