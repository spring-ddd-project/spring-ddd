package com.springddd.domain.gen;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenAggregateDomain extends AbstractDomainMask {

    private AggregateId aggregateId;

    private InfoId infoId;

    private GenAggregateValueObject valueObject;

    private GenAggregateExtendInfo extendInfo;

    private com.springddd.domain.gen.state.AggregateState state;

    public void setState(com.springddd.domain.gen.state.AggregateState state) {
        this.state = state;
    }

    public void create() {
        this.state = new com.springddd.domain.gen.state.ActiveState();
    }

    public void update(InfoId infoId, GenAggregateValueObject valueObject, GenAggregateExtendInfo extendInfo) {
        this.infoId = infoId;
        this.valueObject = valueObject;
        this.extendInfo = extendInfo;
    }

    public void delete() {
        if (state == null) state = getDeleteStatus() ? new com.springddd.domain.gen.state.DeletedState() : new com.springddd.domain.gen.state.ActiveState();
        state.delete(this);
    }

    public void restore() {
        if (state == null) state = getDeleteStatus() ? new com.springddd.domain.gen.state.DeletedState() : new com.springddd.domain.gen.state.ActiveState();
        state.restore(this);
    }
}

















































