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

    public void create() {}

    public void update(InfoId infoId, GenAggregateValueObject valueObject, GenAggregateExtendInfo extendInfo) {
        this.infoId = infoId;
        this.valueObject = valueObject;
        this.extendInfo = extendInfo;
    }
}
